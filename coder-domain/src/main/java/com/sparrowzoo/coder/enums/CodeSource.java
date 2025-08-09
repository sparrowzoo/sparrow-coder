package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import com.sparrowzoo.coder.constant.EnumNames;

@EnumUniqueName(name = EnumNames.CODE_SOURCE)
public enum CodeSource implements EnumIdentityAccessor {
    INNER(1),
    SOURCE_CODE(2);

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
