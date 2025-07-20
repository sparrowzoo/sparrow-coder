package com.sparrowzoo.coder.protocol.enums;

import com.sparrow.protocol.ModuleSupport;

public class CoderModule {
    public static final ModuleSupport Coder = new ModuleSupport() {
        @Override
        public String code() {
            return "99";
        }

        @Override
        public String name() {
            return "Coder";
        }
    };
}
