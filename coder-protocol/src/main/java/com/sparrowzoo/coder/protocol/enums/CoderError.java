package com.sparrowzoo.coder.protocol.enums;

import com.sparrow.protocol.ErrorSupport;
import com.sparrow.protocol.ModuleSupport;
import lombok.Getter;

@Getter
public enum CoderError implements ErrorSupport {
    NOT_SELF_PROJECT(false, CoderModule.Coder, "03", "can't generate other's project"),
    SYSTEM_TABLE(false, CoderModule.Coder, "02", "system table can't be modified"),
    CLASS_NOT_FOUND(false, CoderModule.Coder, "01", "class not found");

    private boolean system;
    private ModuleSupport module;
    private String code;
    private String message;

    CoderError(boolean system, ModuleSupport module, String code, String message) {
        this.system = system;
        this.message = message;
        this.module = module;
        this.code = (system ? 0 : 1) + module.code() + code;
    }

    @Override
    public boolean system() {
        return this.system;
    }

    @Override
    public ModuleSupport module() {
        return this.module;
    }
}
