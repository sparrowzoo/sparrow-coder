/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.coder.domain.service;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.sparrow.coder.domain.bo.ProjectConfigBO;
import com.sparrow.coder.protocol.dto.CreateTableResult;
import com.sparrow.coder.protocol.dto.CreateTableResult.Status;
import com.sparrow.coder.protocol.enums.CoderError;
import com.sparrow.coder.protocol.query.TableConfigQuery;
import com.sparrow.coder.repository.ProjectConfigRepository;
import com.sparrow.coder.repository.TableConfigRepository;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.enums.StatusRecord;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Named
@Slf4j
public class TableCreationService {
    private final DataSource dataSource;
    private final ProjectConfigRepository projectRepository;
    private final TableConfigRepository tableRepository;
    private final EnvConfig envConfig;

    @Inject
    public TableCreationService(DataSource dataSource, ProjectConfigRepository projectRepository,
                                TableConfigRepository tableRepository, EnvConfig envConfig) {
        this.dataSource = dataSource;
        this.projectRepository = projectRepository;
        this.tableRepository = tableRepository;
        this.envConfig = envConfig;
    }

    public List<CreateTableResult> createTables(Long projectId, String[] tableNames, Long userId) throws BusinessException {
        if (projectId == null || tableNames == null || tableNames.length == 0) {
            throw new IllegalArgumentException("Select a project and at least one table");
        }
        ProjectConfigBO project = projectRepository.getProjectConfig(projectId);
        if (project == null || userId == null || !Objects.equals(project.getCreateUserId(), userId)) {
            throw new BusinessException(CoderError.NOT_SELF_PROJECT);
        }
        TableConfigQuery query = new TableConfigQuery();
        query.setProjectId(projectId);
        query.setStatus(StatusRecord.ENABLE.getIdentity());
        query.allPageSize();
        Set<String> allowedTables = new HashSet<>();
        tableRepository.queryTableConfigs(query).forEach(table -> allowedTables.add(table.getTableName()));
        Path projectHome = Path.of(envConfig.getProjectHome(project)).toAbsolutePath().normalize();
        List<CreateTableResult> results = new ArrayList<>();
        for (String tableName : tableNames) {
            if (tableName == null || !tableName.matches("[A-Za-z_][A-Za-z0-9_]*") || !allowedTables.contains(tableName)) {
                results.add(new CreateTableResult(tableName, Status.FAILED, "TABLE_NOT_IN_PROJECT"));
                continue;
            }
            results.add(createTable(projectHome, tableName));
        }
        return results;
    }

    private CreateTableResult createTable(Path projectHome, String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            if (tableExists(connection, tableName)) {
                return new CreateTableResult(tableName, Status.SKIPPED, "ALREADY_EXISTS");
            }
            Path ddlDirectory = projectHome.resolve("ddl").toRealPath();
            Path script = ddlDirectory.resolve(tableName + ".sql").toRealPath();
            if (!ddlDirectory.startsWith(projectHome.toRealPath()) || !script.startsWith(ddlDirectory)) {
                return new CreateTableResult(tableName, Status.FAILED, "INVALID_DDL");
            }
            String ddl = Files.readString(script, StandardCharsets.UTF_8);
            SQLCreateTableStatement create = parseCreateTable(ddl, tableName);
            // Execute only the validated CREATE statement; generated scripts also contain DROP TABLE.
            try (Statement statement = connection.createStatement()) {
                statement.execute(SQLUtils.toSQLString(create, DbType.mysql));
            }
            return new CreateTableResult(tableName, Status.CREATED, "CREATED");
        } catch (NoSuchFileException e) {
            return new CreateTableResult(tableName, Status.FAILED, "DDL_NOT_FOUND");
        } catch (IllegalArgumentException e) {
            return new CreateTableResult(tableName, Status.FAILED, "INVALID_DDL");
        } catch (SQLException e) {
            // Another request may have created the same table after the metadata check.
            if (e.getErrorCode() == 1050 || "42S01".equals(e.getSQLState())) {
                return new CreateTableResult(tableName, Status.SKIPPED, "ALREADY_EXISTS");
            }
            log.error("Failed to create table {}", tableName, e);
            return new CreateTableResult(tableName, Status.FAILED, "CREATE_FAILED");
        } catch (IOException e) {
            log.error("Failed to read DDL for table {}", tableName, e);
            return new CreateTableResult(tableName, Status.FAILED, "CREATE_FAILED");
        }
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        String escape = metadata.getSearchStringEscape();
        String pattern = tableName.replace(escape, escape + escape)
                .replace("_", escape + "_").replace("%", escape + "%");
        try (ResultSet tables = metadata.getTables(connection.getCatalog(), null, pattern, new String[]{"TABLE"})) {
            while (tables.next()) {
                if (tableName.equalsIgnoreCase(tables.getString("TABLE_NAME"))) {
                    return true;
                }
            }
        }
        return false;
    }

    private SQLCreateTableStatement parseCreateTable(String ddl, String tableName) {
        SQLCreateTableStatement create = null;
        List<SQLStatement> statements;
        try {
            statements = SQLUtils.parseStatements(ddl, DbType.mysql);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid DDL", e);
        }
        for (SQLStatement statement : statements) {
            if (statement instanceof SQLDropTableStatement drop && drop.getTableSources().size() == 1
                    && matchesTable(drop.getTableSources().getFirst().getExpr(), tableName)) {
                continue;
            }
            if (!(statement instanceof SQLCreateTableStatement candidate) || create != null
                    || !matchesTable(candidate.getName(), tableName) || candidate.getSelect() != null
                    || candidate.getLike() != null || candidate.isTemporary() || candidate.isReplace()
                    || candidate.getTableElementList().isEmpty()) {
                throw new IllegalArgumentException("Only CREATE TABLE for the selected table is allowed");
            }
            create = candidate;
        }
        if (create == null) {
            throw new IllegalArgumentException("Missing CREATE TABLE");
        }
        return create;
    }

    private boolean matchesTable(SQLExpr name, String tableName) {
        return name instanceof SQLIdentifierExpr identifier && tableName.equals(SQLUtils.normalize(identifier.getName()));
    }
}
