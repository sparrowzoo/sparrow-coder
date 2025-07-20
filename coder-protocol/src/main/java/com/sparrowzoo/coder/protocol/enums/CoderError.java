package com.sparrowzoo.coder.protocol.enums;

import com.sparrow.protocol.ErrorSupport;
import com.sparrow.protocol.ModuleSupport;
import lombok.Getter;

@Getter
public enum CoderError implements ErrorSupport {

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
