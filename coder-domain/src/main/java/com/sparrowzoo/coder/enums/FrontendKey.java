package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import lombok.Getter;

/**
 * 前端组件模板枚举
 */
@Getter
public enum FrontendKey implements EnumIdentityAccessor {
    PAGE("page.txt", 1),
    API("api.txt", 2),
    ADD("add.txt", 3),
    EDIT("edit.txt", 4),
    SEARCH("search.txt", 5),
    OPERATION("operation.txt", 6),
    COLUMNS("columns.txt", 7),
    MESSAGE("", 8),
    MESSAGE_FILE_LIST("", 9),
    SCHEMA("schema.txt", 10),
    ROW_OPERATION("row-operation.txt", 11);


    private String template;
    private Integer id;

    FrontendKey(String template, Integer id) {
        this.id = id;
        this.template = template;
    }

    @Override
    public Integer getIdentity() {
        return this.id;
    }

    public static FrontendKey getEnum(Integer id) {
        for (FrontendKey key : FrontendKey.values()) {
            if (key.getIdentity().equals(id)) {
                return key;
            }
        }
        return null;
    }
}
