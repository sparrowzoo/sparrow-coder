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

package com.sparrow.coder.domain.service.frontend.generator.column;

import com.sparrow.coder.constant.ArchitectureNames;
import com.sparrow.coder.domain.bo.ColumnDef;
import com.sparrow.coder.domain.bo.ProjectBO;
import com.sparrow.coder.enums.CellType;
import com.sparrow.coder.enums.ControlType;
import com.sparrow.coder.enums.HeaderType;
import com.sparrow.lang.joiner.StringJoiner;
import com.sparrow.utility.StringUtility;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;

@Named
public class ReactColumnGenerator extends AbstractColumnGenerator {
    /**
     * {
     * id: "expander",
     * enableHiding: false,
     * header: EmptyHeader,
     * cell: TreeCell("status")
     * }
     *
     * @param columnDef
     * @return
     */
    @Override
    public String column(ColumnDef columnDef, ProjectBO project) {
        ColumnConfig config = new ColumnConfig();
        HeaderType headerType = HeaderType.getById(columnDef.getHeaderType());
        if (headerType != null) {
            config.setId(headerType.getSpecialId());
            config.setAccessorKey(columnDef.getPropertyName());
            config.setHeader(this.getHeader(columnDef));
        }
        config.setEnableHiding(columnDef.getEnableHidden());
        config.setCell(this.getCell(columnDef));

        List<String> columnDefList = new ArrayList<>();
        if (!StringUtility.isNullOrEmpty(config.getId())) {
            columnDefList.add(String.format(" id: \"%s\"", config.getId()));
        } else if (!StringUtility.isNullOrEmpty(config.getAccessorKey())) {
            columnDefList.add(String.format(" accessorKey: \"%s\"", config.getAccessorKey()));
        }
        columnDefList.add(String.format("header: %s", config.getHeader()));
        columnDefList.add(String.format("cell:%s", config.getCell()));
        columnDefList.add(String.format("enableHiding: %s", config.isEnableHiding()));
        String columns = new StringJoiner(",\n", columnDefList).join();
        return String.format("{\n%s\n}", columns);
    }

    @Override
    public String importEdit(ColumnDef columnDef, ProjectBO project) {
        ControlType controlType = ControlType.getControlType(columnDef.getControlType());
        if (controlType == null) {
            return "";
        }
        return String.format("import {%1$s} from \"@/common/components/forms/%2$s\";", controlType.getComponent(), controlType.getFileName());
    }


    /**
     * <ValidatableInput {...register("name")}
     * type={"text"}
     * isSubmitted={isSubmitted}
     * pageTranslate={pageTranslate}
     * validateTranslate={validateTranslate}
     * errorMessage={errors.name?.message}
     * fieldPropertyName={"name"}/>
     *
     * @param columnDef
     * @return
     */
    @Override
    public String edit(ColumnDef columnDef, ProjectBO project, Boolean add) {

        if (HeaderType.MENU.getIdentity().equals(columnDef.getHeaderType()) ||
                HeaderType.CHECK_BOX.getIdentity().equals(columnDef.getHeaderType()) ||
                HeaderType.COLUMN_FILTER.getIdentity().equals(columnDef.getHeaderType())
        ) {
            return "";
        }
        if (columnDef.getControlType().equals(ControlType.INPUT_HIDDEN.getIdentity())) {
            return String.format("<ValidatableInput %3$s {...register(\"%1$s\")}\n" +
                            "                                  type={\"%2$s\"}\n" +
                            "                                  fieldPropertyName={\"%1$s\"}/>",
                    columnDef.getPropertyName(),
                    ControlType.getControlType(columnDef.getControlType()).getInputType(),
                    this.defaultValue(columnDef, add)
            );
        }

        if (columnDef.getControlType().equals(ControlType.SELECT.getIdentity())) {
            return String.format("<ValidatableSelect " +
                            "dictionary={pageResult.dictionary[\"%1$s\"]} " +
                            "pageTranslate={pageTranslate} " +
                            "%2$s" +
                            "setValue={setValue}\n" +
                            "errorMessage={errors.%1$s?.message}\n" +
                            "isSubmitted={isSubmitted}\n" +
                            "setError={setError}\n" +
                            "fieldPropertyName={\"%1$s\"}/>",
                    columnDef.getPropertyName(),
                    this.defaultValue(columnDef, add));
        }

        if (columnDef.getControlType().equals(ControlType.IMAGE.getIdentity())) {
            return String.format("""
                            <ValidatableImage readonly={false} %2$s
                                                              pathType={"%3$s"}
                                                              setValue={setValue}
                                                              isSubmitted={isSubmitted}
                                                              pageTranslate={pageTranslate}
                                                              errorMessage={errors.%1$s?.message}                                  \
                            fieldPropertyName={"%1$s"}/>
                            """,
                    columnDef.getPropertyName(),
                    this.defaultValue(columnDef, add),
                    columnDef.getDatasourceParams());
        }

        if (columnDef.getControlType().equals(ControlType.DATE.getIdentity())) {
            return String.format("<ValidatableDate readonly={false} fieldPropertyName={\"%1$s\"}\n" +
                            "                                 setValue={setValue}\n" +
                            "                                 pageTranslate={pageTranslate} %2$s/>",
                    columnDef.getPropertyName(),
                    this.defaultValue(columnDef, add));
        }

        String message = "";
        if (!StringUtility.isNullOrEmpty(columnDef.getValidateType()) &&
                !columnDef.getValidateType().equals("nullableValidatorMessageGenerator")) {
            message = String.format("errorMessage={errors.%1$s?.message}", columnDef.getPropertyName());
        }

        if (columnDef.getControlType().equals(ControlType.TEXT_AREA.getIdentity())) {
            return String.format("<ValidatableTextarea className={\"w-80 h-60\"} readonly={%4$s} %3$s {...register(\"%1$s\")}\n" +
                            "                                  isSubmitted={isSubmitted}\n" +
                            "                                  pageTranslate={pageTranslate}\n" +
                            "                                  %2$s" +
                            "                                  fieldPropertyName={\"%1$s\"}/>",
                    columnDef.getPropertyName(),
                    message,
                    this.defaultValue(columnDef, add),
                    columnDef.getReadOnly() != null && columnDef.getReadOnly()
            );
        }


        return String.format("<ValidatableInput readonly={%5$s} %4$s {...register(\"%1$s\")}\n" +
                        "                                  type={\"%2$s\"}\n" +
                        "                                  isSubmitted={isSubmitted}\n" +
                        "                                  pageTranslate={pageTranslate}\n" +
                        "                                  %3$s" +
                        "                                  fieldPropertyName={\"%1$s\"}/>",
                columnDef.getPropertyName(),
                ControlType.getControlType(columnDef.getControlType()).getInputType(),
                message,
                this.defaultValue(columnDef, add),
                columnDef.getReadOnly() != null && columnDef.getReadOnly()
        );
    }

    private String defaultValue(ColumnDef columnDef, Boolean add) {
        if (add) {
            return String.format("defaultValue={\"%s\"}", columnDef.getDefaultValue());
        }
        if (columnDef.getControlType().equals(ControlType.CHECK_BOX.getIdentity())) {
            return String.format("defaultChecked={original.%s}", columnDef.getPropertyName());
        }
        return String.format("defaultValue={original.%s}", columnDef.getPropertyName());
    }

    @Override
    public String importHeader(HeaderType headerType, ProjectBO project) {
        return String.format("import %1$s from \"@/common/components/table/header/%2$s\";", headerType.getComponentName(), headerType.getFileName());
    }

    @Override
    public String importCell(CellType cellType, ProjectBO project) {
        return String.format("import %1$s from \"@/common/components/table/cell/%2$s\";", cellType.getComponentName(), cellType.getFileName());
    }

    @Override
    public String getName() {
        return ArchitectureNames.REACT;
    }

    @Override
    public String columnDefs(String className, List<String> columns) {
        String columnStr = String.join(",", columns);
        return String.format("export const columns: ColumnDef<%1$s>[] = [\n%2$s\n];", className, columnStr);
    }


    private String getHeader(ColumnDef columnDef) {
        HeaderType headerType = HeaderType.getById(columnDef.getHeaderType());
        if (headerType == null) {
            return "";
        }
        String columnTitle = columnDef.getChineseName();
        return switch (headerType) {
            case CHECK_BOX, EMPTY -> headerType.getComponentName();
            case COLUMN_FILTER -> headerType.getComponentName() + "()";
            case MENU, NORMAL,TREE ->
                    String.format("PlainTextHeader({columnTitle: \"%1$s\"} as ColumnOperationProps)", columnTitle);
            case NORMAL_FILTER, NORMAL_SORT, NORMAL_SORT_FILTER -> String.format("NormalHeader({\n" +
                            "            showSort: %1$s,\n" +
                            "            showFilter: %2$s,\n" +
                            "            columnTitle: \"%3$s\",\n" +
                            "        } as ColumnOperationProps)%4$s",
                    headerType.getSortable(),
                    headerType.getFilterable(),
                    columnTitle,
                    headerType.getSortable() ? ",\nfilterFn: filterFns.includesString" : "");
            default -> "''";
        };
    }

    private String getCell(ColumnDef columnDef) {
        HeaderType headerType = HeaderType.getById(columnDef.getHeaderType());

        if (HeaderType.COLUMN_FILTER.equals(headerType)) {
            return "\"\"";
        }
        if (HeaderType.CHECK_BOX.equals(headerType)) {
            columnDef.setCellType(CellType.CHECK_BOX.getIdentity());
            return CellType.CHECK_BOX.getComponentName();
        }
        if (HeaderType.TREE.equals(headerType)) {
            columnDef.setCellType(CellType.TREE.getIdentity());
            return String.format(" %1$s(\"%2$s\")", CellType.TREE.getComponentName(), columnDef.getPropertyName());
        }
        if (HeaderType.MENU.equals(headerType)) {
            columnDef.setCellType(CellType.OPERATION.getIdentity());
            return "'Actions'";
        }
        CellType cellType = CellType.getById(columnDef.getCellType());
        if (cellType == null) {
            return "''";
        }
        switch (cellType) {
            case TREE:
            case NORMAL:
            case UNIX_TIMESTAMP:
            case IMAGE:
                return String.format(" %1$s(\"%2$s\")", cellType.getComponentName(), columnDef.getPropertyName());
            case CHECK_BOX:
                return CellType.CHECK_BOX.getComponentName();
            case CURRENCY:
                return String.format(" %3$s(\"%1$s\", \"%2$s\")", columnDef.getPropertyName(), "USD", CellType.CURRENCY.getComponentName());
            case OPERATION:
                return "\"Actions\"";
            default:
                return "''";
        }
    }
}
