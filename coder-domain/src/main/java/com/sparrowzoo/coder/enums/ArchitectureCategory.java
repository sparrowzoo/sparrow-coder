package com.sparrowzoo.coder.enums;

public enum ArchitectureCategory {
    BACKEND("后端"),
    FRONTEND("前端"),
    MOBILE("移动端"),
    DESKTOP("桌面"),
    MINI_PROGRAM("小程序"),
    DATABASE("数据库"),;

    private String name;

    ArchitectureCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
