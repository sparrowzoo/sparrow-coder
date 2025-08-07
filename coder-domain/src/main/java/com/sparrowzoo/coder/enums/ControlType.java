package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import com.sparrowzoo.coder.constant.EnumNames;
import lombok.Getter;

@Getter
@EnumUniqueName(name = EnumNames.CONTROL_TYPE)
public enum ControlType implements EnumIdentityAccessor {
    INPUT_TEXT("txt", "text", 1),
    LABEL("lbl", "label", 2),
    LINK("lnk", "link", 3),
    INPUT_HIDDEN("hdn", "hidden", 4),
    INPUT_PASSWORD("txt", "password", 5),
    TEXT_AREA("txt", "textarea", 6),
    SELECT("ddl", "select", 7),
    CODE("txt", "text", 10),
    EDITOR("divEditor", "editor", 11),
    DATE("txt", "date", 12),
    DATE_HHMMSS("txt", "time", 13),
    CHECK_BOX("ckb", "checkbox", 14),
    RADIO("rdb", "radio", 15),
    FILE("flb", "file", 16),
    ENABLE_DISABLE("", "button", 17),
    IMAGE("img", "image", 19);


    private final String prefix;
    private final String inputType;
    private Integer id;


    ControlType(String prefix, String inputType, Integer id) {
        this.prefix = prefix;
        this.inputType = inputType;
        this.id = id;
    }

    @Override
    public Integer getIdentity() {
        return id;
    }

    public static ControlType getControlType(Integer inputType) {
        for (ControlType controlType : ControlType.values()) {
            if (controlType.id.equals(inputType)) {
                return controlType;
            }
        }
        return null;
    }
}
