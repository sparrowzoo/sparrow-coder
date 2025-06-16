package com.sparrowzoo.coder.bo;

import lombok.Data;

@Data
public class Entity {
    /**
     * 实体类名
     */
    private String name;
    /**
     * tableName
     * Map<String,Table> tableMap
     * key: tableName
     * value: Table JPA对象
     */
    private String tableName;

    private String text;

    private String i18n;
}
