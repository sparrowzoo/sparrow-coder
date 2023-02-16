package com.sparrow.coding.frontend.validate2;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.EmailValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.NullValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.TelValidatorMessageGenerator;
import com.sparrow.coding.protocol.validate.EmailValidator;
import com.sparrow.coding.protocol.validate.NullValidator;
import com.sparrow.coding.protocol.validate.TelValidator;
import java.lang.annotation.Annotation;

/**
 * 如何我再加新的验证注解，这个类还需要加if else 判断，该策略比较简单粗暴 不够优雅F
 */
public class MessageGeneratorStrategyV2 {
    public static ValidatorMessageGenerator getValidatorGenerator(Class<? extends Annotation> clazz) {
        if (clazz.equals(NullValidator.class)) {
            return new NullValidatorMessageGenerator();
        }
        if (clazz.equals(EmailValidator.class)) {
            return new EmailValidatorMessageGenerator();
        }
        if(clazz.equals(TelValidator.class)){
            return new TelValidatorMessageGenerator();
        }
        return null;
    }
}
