package com.sparrow.coding.frontend.validate3;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.EmailValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.NullValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.TelValidatorMessageGenerator;
import com.sparrow.coding.protocol.validate.EmailValidator;
import com.sparrow.coding.protocol.validate.NullValidator;
import com.sparrow.coding.protocol.validate.TelValidator;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class MessageGeneratorStrategyV3 {
    /**
     * key 验证注解的类
     * <p>
     * value 验证注解的信息生成器
     */
    private static Map<Class<? extends Annotation>, ValidatorMessageGenerator> strategies = new HashMap<>();

    public static void put(Class<? extends Annotation> clazz, ValidatorMessageGenerator validate) {
        strategies.put(clazz, validate);
    }

    public static ValidatorMessageGenerator getValidatorGenerator(Class<? extends Annotation> clazz) {
        return strategies.get(clazz);
    }
}
