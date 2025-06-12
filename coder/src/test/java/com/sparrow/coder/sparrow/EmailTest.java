package com.sparrow.coder.sparrow;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.api.ValidatorRegistry;
import com.sparrow.coding.protocol.validate.RegexValidator;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import org.junit.jupiter.api.Test;

public class EmailTest {
    @Test
    public void email() {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder().initController(false)
                .initInterceptor(false)
                .scanBasePackage("com.sparrow"));
        ValidatorMessageGenerator messageGenerator = ValidatorRegistry.getInstance().getValidatorMessageGenerator("valibot",
                "emailValidatorMessageGenerator");

        RegexValidator validator = new RegexValidator();
        validator.setAllowEmpty(false);
        validator.setI18n(false);
        validator.setEmptyMessage("邮箱不能为空");
        validator.setMinLength(5);
        validator.setMaxLength(30);
        validator.setFormatMessage("请输入正确的邮箱格式");
        validator.setMinLengthMessage("邮箱长度必须在5到30之间");
        validator.setMaxLengthMessage("邮箱长度必须在5到30之间");

        String validateMessage = messageGenerator.generateConfig("email", validator);
        System.out.println(validateMessage);
    }
}
