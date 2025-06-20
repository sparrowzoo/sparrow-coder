package com.sparrowzoo.coder.domain.service.frontend.generator.column;

import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.enums.CellType;
import com.sparrowzoo.coder.enums.HeaderType;

public class ReactColumnGenerator extends AbstractColumnGenerator{
    @Override
    public String column(ColumnDef columnDef) {

    }

    public String renderHeader(HeaderType headerType, String columnTitle, String i18nPrefix) {
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
                        headerType.getSortable() ? ";\nfilterFn: filterFns.includesString" : "");
            default:
                return "";
        }
    }

    public String renderCell(CellType cellType, String columnName, String currency) {
        switch (cellType) {
            case TREE:
                return String.format("cell: TreeCell(\"%s\")", columnName);
            case CHECK_BOX:
                return "cell: CheckBoxCell";
            case NORMAL:
                return String.format("cell: NormalCell(\"%s\")", columnName);
            case CURRENCY:
                return String.format("cell: CurrencyCell(\"%1$s\", \"%2$s\")", columnName, currency);
            case OPERATION:
                return "cell:Actions";
            default:
                return "";
        }
    }
}
