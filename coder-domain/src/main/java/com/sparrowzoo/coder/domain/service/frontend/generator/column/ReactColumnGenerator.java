package com.sparrowzoo.coder.domain.service.frontend.generator.column;

import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.constant.ArchitectureNames;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.enums.CellType;
import com.sparrowzoo.coder.enums.ColumnType;
import com.sparrowzoo.coder.enums.ControlType;
import com.sparrowzoo.coder.enums.HeaderType;

import javax.inject.Named;
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
        List<String> columnDefList = new ArrayList<>();
        columnDefList.add(this.renderColumnType(columnDef));
        columnDefList.add(this.renderHeader(columnDef,project));
        columnDefList.add(this.renderCell(columnDef));
        columnDefList.add(String.format("enableHiding: %s", columnDef.getEnableHidden()));
        return String.format("{\n%s\n}", StringUtility.join(columnDefList, ",\n"));
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
    public String edit(ColumnDef columnDef,ProjectBO project, Boolean add) {
        if (columnDef.getColumnType().equals(ColumnType.ACTION.getIdentity()) ||
                columnDef.getColumnType().equals(ColumnType.CHECK.getIdentity()) ||
                columnDef.getColumnType().equals(ColumnType.FILTER.getIdentity())
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

        String message = "";
        if (!StringUtility.isNullOrEmpty(columnDef.getValidateType()) &&
                !columnDef.getValidateType().equals("nullableValidatorMessageGenerator")) {
            message = String.format("errorMessage={errors.%1$s?.message}", columnDef.getPropertyName());
        }

        if(columnDef.getControlType().equals(ControlType.TEXT_AREA.getIdentity())){
            return String.format("<ValidatableTextArea className={\"w-80 h-60\"} readonly={%4$s} %3$s {...register(\"%1$s\")}\n" +
                            "                                  isSubmitted={isSubmitted}\n" +
                            "                                  pageTranslate={pageTranslate}\n" +
                            "                                  validateTranslate={validateTranslate}\n" +
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
                        "                                  validateTranslate={validateTranslate}\n" +
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
            return "";
        }
        if (columnDef.getControlType().equals(ControlType.CHECK_BOX.getIdentity())) {
            return String.format("defaultChecked={original.%s}", columnDef.getPropertyName());
        }
        return String.format("defaultValue={original.%s}", columnDef.getPropertyName());
    }

    @Override
    public String importHeader(HeaderType headerType,ProjectBO project) {
        return String.format("import %1$s from \"@/common/components/table/header/%2$s\";", headerType.getComponentName(), headerType.getFileName());
    }

    @Override
    public String importCell(CellType cellType,ProjectBO project) {
        return String.format("import %1$s from \"@/common/components/table/cell/%2$s\";", cellType.getComponentName(), cellType.getFileName());
    }

    @Override
    public String getName() {
        return ArchitectureNames.REACT;
    }


    public String renderColumnType(ColumnDef columnDef) {
        ColumnType columnType =ColumnType.getById(columnDef.getColumnType());
        switch (columnType) {
            case ACTION:
                return " id: \"actions\"";
            case FILTER:
                return " id: \"filter-column\"";
            case TREE:
                return " id: \"expander\"";
            case CHECK:
                return " id: \"select\"";
            case NORMAL:
                return String.format(" accessorKey: \"%s\"", columnDef.getPropertyName());
            default:
                return "";
        }
    }

    public String renderHeader(ColumnDef columnDef,ProjectBO project) {
        HeaderType headerType =HeaderType.getById(columnDef.getHeaderType());
        if (headerType == null) {
            return "header:''";
        }
        String columnTitle = columnDef.getChineseName();
        if (ColumnType.CHECK.getId().equals(columnDef.getColumnType())) {
            return "header: CheckboxHeader";
        }
        if (ColumnType.FILTER.getId().equals(columnDef.getColumnType())) {
            return "header: ColumnFilter()";

        }
        if (ColumnType.TREE.getId().equals(columnDef.getColumnType())) {
            return "header:\"\"";
        }
        if (ColumnType.ACTION.getId().equals(columnDef.getColumnType())) {
            return String.format("header: PlainTextHeader({columnTitle: \"%1$s\"} as ColumnOperationProps)", columnTitle);
        }
        switch (headerType) {
            case CHECK_BOX:
                return "header: CheckboxHeader";
            case EMPTY:
                return "header: EmptyHeader";
            case COLUMN_FILTER:
                return "header: ColumnFilter";
            case PLAIN_TEXT:
            case NORMAL:
                return String.format("header: PlainTextHeader({columnTitle: \"%1$s\"} as ColumnOperationProps)", columnTitle);
            case NORMAL_FILTER:
            case NORMAL_SORT:
            case NORMAL_SORT_FILTER:
                return String.format("header: NormalHeader({\n" +
                                "            showSort: %1$s,\n" +
                                "            showFilter: %2$s,\n" +
                                "            columnTitle: \"%3$s\",\n"+
                                "        } as ColumnOperationProps)%4$s",
                        headerType.getSortable(),
                        headerType.getFilterable(),
                        columnTitle,
                        headerType.getSortable() ? ",\nfilterFn: filterFns.includesString" : "");
            default:
                return "header:''";
        }
    }

    public String renderCell(ColumnDef columnDef) {
        if (columnDef.getColumnType().equals(ColumnType.FILTER.getIdentity())) {
            return "cell:\"\"";
        }
        if (columnDef.getColumnType().equals(ColumnType.CHECK.getIdentity())) {
            return "cell:"+CellType.CHECK_BOX.getComponentName();
        }
        if (columnDef.getColumnType().equals(ColumnType.TREE.getIdentity())) {
            return String.format("cell: %1$s(\"%2$ss\")",CellType.TREE.getComponentName(), columnDef.getPropertyName());
        }
        if (columnDef.getColumnType().equals(ColumnType.ACTION.getIdentity())) {
            return "cell:\"Actions\"";
        }
        CellType cellType =CellType.getById(columnDef.getCellType());
        if (cellType == null) {
            return "cell:''";
        }
        String columnName = columnDef.getPropertyName();
        switch (cellType) {
            case TREE:
            case NORMAL:
            case UNIX_TIMESTAMP:
                return String.format("cell: %1$s(\"%2$s\")",cellType.getComponentName(), columnDef.getPropertyName());
            case CHECK_BOX:
                return "cell: "+CellType.CHECK_BOX.getComponentName();
            case CURRENCY:
                return String.format("cell: %3$s(\"%1$s\", \"%2$s\")", columnName, columnDef.getSubsidiaryColumns(), CellType.CURRENCY.getComponentName());
            case OPERATION:
                return "cell:\"Actions\"";
            default:
                return "cell:''";
        }
    }
}
