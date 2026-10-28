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

import com.sparrow.coder.domain.bo.ProjectConfigBO;
import com.sparrow.coder.domain.bo.TableConfigBO;
import com.sparrow.coder.moke.beans.EnvConfigImpl;
import com.sparrow.coder.protocol.dto.CreateTableResult;
import com.sparrow.coder.protocol.query.TableConfigQuery;
import com.sparrow.coder.repository.ProjectConfigRepository;
import com.sparrow.coder.repository.TableConfigRepository;
import com.sparrow.protocol.BusinessException;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TableCreationServiceTest {
    @TempDir
    Path projectHome;
    private JdbcDataSource dataSource;
    private TableCreationService service;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");
        ProjectConfigBO project = new ProjectConfigBO();
        project.setId(1L);
        project.setCreateUserId(7L);
        ProjectConfigRepository projects = (ProjectConfigRepository) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{ProjectConfigRepository.class},
                (proxy, method, args) -> method.getName().equals("getProjectConfig") && Long.valueOf(1).equals(args[0]) ? project : null);
        TableConfigRepository tables = (TableConfigRepository) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class<?>[]{TableConfigRepository.class}, (proxy, method, args) -> {
                    TableConfigQuery query = (TableConfigQuery) args[0];
                    assertEquals(1L, query.getProjectId());
                    return List.of(table("t_new"), table("t_existing"), table("t_missing"), table("t_bad"));
                });
        EnvConfig envConfig = new EnvConfigImpl() {
            @Override
            public String getProjectHome(ProjectConfigBO config) {
                return projectHome.toString();
            }
        };
        service = new TableCreationService(dataSource, projects, tables, envConfig);
        Files.createDirectories(projectHome.resolve("ddl"));
    }

    @Test
    void createsSelectedTableWithoutExecutingGeneratedDropStatement() throws Exception {
        script("t_new", "DROP TABLE IF EXISTS `t_new`; CREATE TABLE `t_new` (id BIGINT PRIMARY KEY, note VARCHAR(50) COMMENT 'a;b');");
        List<CreateTableResult> results = service.createTables(1L, new String[]{"t_new"}, 7L);
        assertEquals(CreateTableResult.Status.CREATED, results.getFirst().getStatus());
        assertEquals(0, count("t_new"));
    }

    @Test
    void skipsExistingTableAndPreservesItsDataEvenWithoutScript() throws Exception {
        sql("CREATE TABLE t_existing (id BIGINT); INSERT INTO t_existing VALUES (42)");
        assertEquals(CreateTableResult.Status.SKIPPED,
                service.createTables(1L, new String[]{"t_existing"}, 7L).getFirst().getStatus());
        assertEquals(1, count("t_existing"));
    }

    @Test
    void rejectsOtherUsersProjectBeforeExecutingSql() throws Exception {
        script("t_new", "CREATE TABLE t_new (id BIGINT)");
        assertThrows(BusinessException.class, () -> service.createTables(1L, new String[]{"t_new"}, 8L));
        assertFalse(exists("t_new"));
    }

    @Test
    void continuesAfterMissingScriptAndUnknownTable() throws Exception {
        script("t_new", "CREATE TABLE t_new (id BIGINT)");
        List<CreateTableResult> results = service.createTables(1L, new String[]{"t_missing", "../outside", "t_new"}, 7L);
        assertEquals("DDL_NOT_FOUND", results.get(0).getMessage());
        assertEquals("TABLE_NOT_IN_PROJECT", results.get(1).getMessage());
        assertEquals(CreateTableResult.Status.CREATED, results.get(2).getStatus());
        assertTrue(exists("t_new"));
    }

    @Test
    void rejectsExtraStatementsBeforeAnySqlIsExecuted() throws Exception {
        sql("CREATE TABLE t_existing (id BIGINT); INSERT INTO t_existing VALUES (42)");
        script("t_new", "CREATE TABLE t_new (id BIGINT); DELETE FROM t_existing;");
        assertEquals("INVALID_DDL", service.createTables(1L, new String[]{"t_new"}, 7L).getFirst().getMessage());
        assertFalse(exists("t_new"));
        assertEquals(1, count("t_existing"));
    }

    @Test
    void rejectsDifferentOrQualifiedTableAndCreateAsSelect() throws Exception {
        for (String ddl : List.of("CREATE TABLE t_other (id BIGINT)",
                "CREATE TABLE other.t_new (id BIGINT)", "CREATE TABLE t_new AS SELECT 1 AS id",
                "DROP TABLE t_existing; CREATE TABLE t_new (id BIGINT)")) {
            script("t_new", ddl);
            assertEquals("INVALID_DDL", service.createTables(1L, new String[]{"t_new"}, 7L).getFirst().getMessage(), ddl);
            assertFalse(exists("t_new"));
        }
    }

    @Test
    void continuesAfterDatabaseRejectsOneTable() throws Exception {
        script("t_bad", "CREATE TABLE t_bad (id unknown_type)");
        script("t_new", "CREATE TABLE t_new (id BIGINT)");
        List<CreateTableResult> results = service.createTables(1L, new String[]{"t_bad", "t_new"}, 7L);
        assertEquals("CREATE_FAILED", results.getFirst().getMessage());
        assertEquals(CreateTableResult.Status.CREATED, results.get(1).getStatus());
    }

    @Test
    void rejectsEmptySelection() {
        assertThrows(IllegalArgumentException.class, () -> service.createTables(1L, new String[0], 7L));
    }

    private TableConfigBO table(String name) {
        TableConfigBO table = new TableConfigBO();
        table.setTableName(name);
        table.setProjectId(1L);
        return table;
    }

    private void script(String table, String ddl) throws Exception {
        Files.writeString(projectHome.resolve("ddl").resolve(table + ".sql"), ddl);
    }

    private void sql(String sql) throws Exception {
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private long count(String table) throws Exception {
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM " + table)) {
            result.next();
            return result.getLong(1);
        }
    }

    private boolean exists(String table) throws Exception {
        try (Connection connection = dataSource.getConnection();
             ResultSet result = connection.getMetaData().getTables(connection.getCatalog(), null, table, new String[]{"TABLE"})) {
            return result.next();
        }
    }
}
