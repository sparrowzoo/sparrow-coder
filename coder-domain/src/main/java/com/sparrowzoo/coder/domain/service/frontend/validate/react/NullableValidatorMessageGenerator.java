package com.sparrowzoo.coder.domain.service.frontend.validate.react;

import com.sparrowzoo.coder.domain.bo.validate.NoneValidator;
import com.sparrowzoo.coder.domain.bo.validate.RegexValidator;

import javax.inject.Named;

@Named
public class NullableValidatorMessageGenerator extends AbstractValidatorMessageGenerator<NoneValidator> {

    //https://www.66zan.cn/regexdso/
    @Override
    public String outerGenerateMessage(String propertyName, NoneValidator validator) {
        if (validator.getClazz().equals(Boolean.class.getName())) {
            return "v.boolean()";
        }
        if (validator.getClazz().equals(String.class.getName())) {
            return "v.string()";
        }
        if (validator.getClazz().equals(Integer.class.getName()) || validator.getClazz().equals(Long.class.getName()) || validator.getClazz().equals(Double.class.getName()) || validator.getClazz().equals(Float.class.getName())) {
            return "v.number()";
        }
        return "v.any()";
    }

    @Override
    public NoneValidator defaultValidator() {
        return new NoneValidator();
    }
}
