package com.sparrow.coding.java.enums;

/**
 * 生成类的key 通过该key 的配置找到对应的子模块名，包名及类名
 * <p>
 * 具体的配置见config.properties
 */
public enum ClassKey {
    
    PO("", "persistence"),
    BO("bo.txt", "domain"),
    PARAM("param.txt", "protocol"),
    QUERY("query.txt", "protocol"),
    DTO("dto.txt", "protocol"),
    CONTROLLER("controller.txt", "adapter"),
    VO("vo.txt", "adapter"),
    DAO("dao.txt", "dao.api"),
    DAO_IMPL("daoImpl.txt", "dao.sparrow"),
    SERVICE("service.txt", "domain"),
    ASSEMBLE("assemble.txt", "adapter"),
    REPOSITORY("repository.txt", "domain"),
    REPOSITORY_IMPL("repositoryImpl.txt", "infrastructure"),
    DATA_CONVERTER("dataConverter.txt", "infrastructure");

    private String template;
    private String module;

    public String getTemplate() {
        return template;
    }

    ClassKey(String template, String module) {
        this.template = template;
        this.module = module;
    }

    public String getModule() {
        return module;
    }
}
