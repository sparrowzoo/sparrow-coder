package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import lombok.Getter;

@Getter
public enum ArchitectureCategory implements EnumIdentityAccessor {
    BACKEND(1, "后端"),
    FRONTEND(2, "前端"),
    MOBILE(3, "移动端"),
    DESKTOP(4, "桌面"),
    MINI_PROGRAM(5, "小程序"),
    DATABASE(6, "数据库");

    private String name;
    private Integer id;

    ArchitectureCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getIdentity() {
        return this.id;
    }

    public static ArchitectureCategory getById(Integer id) {
        for (ArchitectureCategory category : values()) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }
}
