package com.sparrow.coding.sparrow;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.api.ValidatorRegistry;
import com.sparrow.coding.protocol.validate.RegexValidator;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import org.junit.jupiter.api.Test;

public class IdCardTest {
    @Test
    public void idCard() {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder().initController(false)
                .initInterceptor(false)
                .scanBasePackage("com.sparrow"));
        ValidatorMessageGenerator messageGenerator = ValidatorRegistry.getInstance().getValidatorMessageGenerator("valibot",
                "idCardValidatorMessageGenerator");

        RegexValidator validator = new RegexValidator();
        validator.setAllowEmpty(true);
        validator.setI18n(false);
        validator.setEmptyMessage("身份证不能为空");
        validator.setFormatMessage("请输入正确的身份证格式");

        String validateMessage = messageGenerator.generateConfig("idCard", validator);
        System.out.println(validateMessage);
    }
}
