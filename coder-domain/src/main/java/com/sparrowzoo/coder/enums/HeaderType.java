package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import com.sparrowzoo.coder.constant.EnumNames;
import lombok.Getter;

@Getter
@EnumUniqueName(name = EnumNames.HEADER_TYPE)
public enum HeaderType implements EnumIdentityAccessor {
    CHECK_BOX("CheckboxHeader", "check-box", false, false, "选择", 1),
    COLUMN_FILTER("ColumnFilter", "column-filter", false, false, "过滤", 2),
    NORMAL("PlainTextHeader", "plain-text", false, false, "标准", 3),
    NORMAL_SORT("NormalHeader", "normal", true, false, "标准(可排序)", 4),
    NORMAL_FILTER("NormalHeader", "normal", false, false, "标准(可过滤)", 5),
    NORMAL_SORT_FILTER("NormalHeader", "normal", true, true, "标准(可过滤,可排序)", 6),
    PLAIN_TEXT("PlainTextHeader", "plain-text", false, false, "操作", 7),
    EMPTY("EmptyHeader", "empty", false, false, "空白", 8);
    private String componentName;
    private String fileName;
    private Boolean sortable;
    private Boolean filterable;
    private String description;
    private Integer id;

    HeaderType(String componentName, String fileName, Boolean sortable, Boolean filterable, String description, Integer id) {
        this.componentName = componentName;
        this.fileName = fileName;
        this.description = description;
        this.sortable = sortable;
        this.filterable = filterable;
        this.id = id;
    }


    @Override
    public Integer getIdentity() {
        return this.id;
    }

    public static HeaderType getById(Integer id) {
        for (HeaderType headerType : HeaderType.values()) {
            if (headerType.getId().equals(id)) {
                return headerType;
            }
        }
        return null;
    }
}
