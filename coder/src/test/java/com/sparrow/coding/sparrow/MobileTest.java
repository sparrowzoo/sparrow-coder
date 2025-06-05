package com.sparrow.coding.sparrow;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.api.ValidatorRegistry;
import com.sparrow.coding.protocol.validate.RegexValidator;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import org.junit.jupiter.api.Test;

public class MobileTest {
    @Test
    public void idCard() {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder().initController(false)
                .initInterceptor(false)
                .scanBasePackage("com.sparrow"));
        ValidatorMessageGenerator messageGenerator = ValidatorRegistry.getInstance().getValidatorMessageGenerator("valibot",
                "mobileValidatorMessageGenerator");

        RegexValidator validator = new RegexValidator();
        validator.setAllowEmpty(false);
        validator.setI18n(false);
        validator.setEmptyMessage("手机号不能为空");
        validator.setFormatMessage("请输入正确的手机号格式");
        String validateMessage = messageGenerator.generateConfig("mobile", validator);
        System.out.println(validateMessage);
    }
}
