package com.sparrowzoo.coder.enums;

/**
 * 前端组件模板枚举
 */
public enum FrontendKey {
    PAGE("page.txt"),
    API("api.txt"),
    ADD("add.txt"),
    EDIT("edit.txt"),
    SEARCH("search.txt"),
    OPERATION("operation.txt"),
    COLUMNS("columns.txt"),
    SCHEMA("schema.txt");


    private String template;
    public String getTemplate() {
        return template;
    }

    FrontendKey(String template) {
        this.template = template;
    }
}
