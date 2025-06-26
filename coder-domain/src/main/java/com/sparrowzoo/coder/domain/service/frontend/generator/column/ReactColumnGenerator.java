package com.sparrowzoo.coder.domain.service.frontend.generator.column;

import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.constant.ArchitectureNames;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
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
    public String column(ColumnDef columnDef) {
        List<String> columnDefList = new ArrayList<>();
        columnDefList.add(this.renderColumnType(columnDef));
        columnDefList.add(this.renderHeader(columnDef));
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
    public String edit(ColumnDef columnDef) {
        if (columnDef.getColumnType().equals(ColumnType.ACTION) ||
                columnDef.getColumnType().equals(ColumnType.CHECK) ||
                columnDef.getColumnType().equals(ColumnType.FILTER)
        ) {
            return "";
        }
        if (columnDef.getControlType().equals(ControlType.INPUT_HIDDEN)) {
            return String.format("<ValidatableInput {...register(\"%1$s\")}\n" +
                            "                                  type={\"%2$s\"}\n" +
                            "                                  fieldPropertyName={\"%1$s\"}/>",
                    columnDef.getPropertyName(),
                    columnDef.getControlType().getInputType()
            );
        }

        String message = "";
        if (!StringUtility.isNullOrEmpty(columnDef.getValidateType()) &&
                !columnDef.getValidateType().equals("nullableValidatorMessageGenerator")) {
            message = String.format("errorMessage={errors.%1$s?.message}", columnDef.getPropertyName());
        }
        return String.format("<ValidatableInput {...register(\"%1$s\")}\n" +
                        "                                  type={\"%2$s\"}\n" +
                        "                                  isSubmitted={isSubmitted}\n" +
                        "                                  pageTranslate={pageTranslate}\n" +
                        "                                  validateTranslate={validateTranslate}\n" +
                        "                                  %3$s" +
                        "                                  fieldPropertyName={\"%1$s\"}/>",
                columnDef.getPropertyName(),
                columnDef.getControlType().getInputType(),
                message

        );
    }

    @Override
    public String importHeader(HeaderType headerType) {
        return String.format("import %1$s from \"@/common/components/table/header/%2$s\";", headerType.getComponentName(), headerType.getFileName());
    }

    @Override
    public String importCell(CellType cellType) {
        return String.format("import %1$s from \"@/common/components/table/cell/%2$s\";", cellType.getComponentName(), cellType.getFileName());
    }

    @Override
    public String getName() {
        return ArchitectureNames.REACT;
    }


    public String renderColumnType(ColumnDef columnDef) {
        switch (columnDef.getColumnType()) {
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

    public String renderHeader(ColumnDef columnDef) {

        HeaderType headerType = columnDef.getHeaderType();
        if (headerType == null) {
            return "header:''";
        }
        String columnTitle = columnDef.getChineseName();
        String i18nPrefix = columnDef.getTableClassName();
        switch (headerType) {
            case CHECK_BOX:
                return "header: CheckboxHeader";
            case EMPTY:
                return "header: EmptyHeader";
            case COLUMN_FILTER:
                return "header: ColumnFilter";
            case PLAIN_TEXT:
            case NORMAL:
                return String.format("header: PlainTextHeader({columnTitle: \"%1$s\", i18nPrefix: \"%2$s\"} as ColumnOperationProps)", columnTitle, i18nPrefix);
            case NORMAL_FILTER:
            case NORMAL_SORT:
            case NORMAL_SORT_FILTER:
                return String.format("header: NormalHeader({\n" +
                                "            showSort: %1$s,\n" +
                                "            showFilter: %2$s,\n" +
                                "            columnTitle: \"%3$s\",\n" +
                                "            i18nPrefix: \"%4$s\",\n" +
                                "        } as ColumnOperationProps)%5$s",
                        headerType.getSortable(),
                        headerType.getFilterable(),
                        columnTitle,
                        i18nPrefix,
                        headerType.getSortable() ? ",\nfilterFn: filterFns.includesString" : "");
            default:
                return "header:''";
        }
    }

    public String renderCell(ColumnDef columnDef) {
        CellType cellType = columnDef.getCellType();
        if (cellType == null) {
            return "cell:''";
        }
        String columnName = columnDef.getPropertyName();
        switch (cellType) {
            case TREE:
                return String.format("cell: TreeCell(\"%s\")", columnName);
            case CHECK_BOX:
                return "cell: CheckBoxCell";
            case NORMAL:
                return String.format("cell: NormalCell(\"%s\")", columnName);
            case CURRENCY:
                return String.format("cell: CurrencyCell(\"%1$s\", \"%2$s\")", columnName, columnDef.getSubsidiaryColumns());
            case OPERATION:
                return "cell:\"Actions\"";
            default:
                return "cell:''";
        }
    }
}
