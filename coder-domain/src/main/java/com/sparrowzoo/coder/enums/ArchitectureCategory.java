package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import lombok.Getter;

@Getter
public enum ArchitectureCategory implements EnumIdentityAccessor {
    BACKEND(1, "后端","clear"),
    FRONTEND(2, "前端","react"),
    MOBILE(3, "移动端","mobile"),
    DESKTOP(4, "桌面","pc"),
    MINI_PROGRAM(5, "小程序","mini-program"),
    DATABASE(6, "数据库","mysql");

    private final String name;
    private final Integer id;
    private final String defaultArch;

    ArchitectureCategory(Integer id, String name,String defaultArch) {
        this.id = id;
        this.name = name;
        this.defaultArch = defaultArch;
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
