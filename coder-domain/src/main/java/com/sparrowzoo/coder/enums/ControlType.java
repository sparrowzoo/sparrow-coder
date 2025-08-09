package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import com.sparrowzoo.coder.constant.EnumNames;
import lombok.Getter;

@Getter
@EnumUniqueName(name = EnumNames.CONTROL_TYPE)
public enum ControlType implements EnumIdentityAccessor {
    INPUT_TEXT("ValidatableInput","validatable-input", "text", 1),
    LABEL("ValidatableInput","validatable-input", "label", 2),
    //    LINK("lnk", "link", 3),
    INPUT_HIDDEN("ValidatableInput","validatable-input", "hidden", 4),
    INPUT_PASSWORD("ValidatableInput","validatable-input", "password", 5),
    TEXT_AREA("ValidatableTextarea","validatable-textarea", "textarea", 6),
    SELECT("ValidatableSelect","validatable-select", "select", 7),
    //    CODE("validatable-editor", "code", 10),
//    EDITOR("validatable-editor", "editor", 11),
    DATE("ValidatableDate","validatable-date", "date", 12),
    DATE_HHMMSS("ValidatableTime","validatable-time", "time", 13),
    CHECK_BOX("ValidatableInput","validatable-input", "checkbox", 14),
//    FILE("validatable-upload", "file", 16),
//    IMAGE("validatable-upload", "image", 19)
    ;


    private final String component;
    private final String fileName;
    private final String inputType;
    private Integer id;


    ControlType(String component,String fileName, String inputType, Integer id) {
        this.fileName= fileName;
        this.component = component;
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
