package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;

public enum CodeSource implements EnumIdentityAccessor {
    INNER(1),
    UPLOAD(2);

    private final int identity;

    private CodeSource(Integer identity) {
        this.identity = identity;
    }

    public Integer getIdentity() {
        return identity;
    }

    public static CodeSource getEnum(Integer identity) {
        for (CodeSource codeSource : CodeSource.values()) {
            if (codeSource.getIdentity().equals(identity)) {
                return codeSource;
            }
        }
        return null;
    }
}
