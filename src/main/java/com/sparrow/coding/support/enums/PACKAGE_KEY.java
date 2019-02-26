package com.sparrow.coding.support.enums;

public enum PACKAGE_KEY {

    CONTROLLER("controller.txt"),

    PO(""),

    PO_UNIQUE(""),

    DAO("dao.txt"),

    DAO_IMPL("daoImpl.txt"),

    SERVICE("service.txt"),

    SERVICE_IMPL("serviceImpl.txt"),

    ASSEMBLE(""),

    DTO(""),

    VO(""),

    SUPPORT_API(""),

    SUPPORT_API_IMPL(""),

    SUPPORT_ENUMS(""),

    SUPPORT_UTILS(""),

    TEST(""),

    CONSTANT("");

    private String template;

    public String getTemplate() {
        return template;
    }

    PACKAGE_KEY(String template) {
        this.template = template;
    }
}
