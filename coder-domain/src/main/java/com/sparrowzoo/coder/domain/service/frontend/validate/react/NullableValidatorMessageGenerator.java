package com.sparrowzoo.coder.domain.service.frontend.validate.react;

import com.sparrowzoo.coder.domain.bo.validate.NoneValidator;
import com.sparrowzoo.coder.domain.bo.validate.RegexValidator;

import javax.inject.Named;

@Named
public class NullableValidatorMessageGenerator extends AbstractValidatorMessageGenerator<NoneValidator> {

    //https://www.66zan.cn/regexdso/
    @Override
    public String outerGenerateMessage(String propertyName, NoneValidator validator) {
        return "v.any()";
    }

    @Override
    public NoneValidator defaultValidator() {
        return new NoneValidator();
    }
}
