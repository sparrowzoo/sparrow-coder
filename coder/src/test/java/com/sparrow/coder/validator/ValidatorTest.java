package com.sparrow.coder.validator;

import com.sparrow.coder.ApplicationBoot;
import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.api.ValidatorRegistry;
import com.sparrow.coding.protocol.validate.RegexValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ApplicationBoot.class})
public class ValidatorTest {
    @Test
    public void test() {
        ValidatorMessageGenerator messageGenerator = ValidatorRegistry.getInstance().getValidatorMessageGenerator("valibot",
                "emailValidatorMessageGenerator");

        RegexValidator validator = new RegexValidator();
        validator.setAllowEmpty(true);
        validator.setI18n(false);
        validator.setEmptyMessage("用户名不能为空");
        validator.setMinLength(5);
        validator.setMaxLength(30);
        validator.setFormatMessage("请输入正确的邮箱格式");
        validator.setMinLengthMessage("用户名长度必须在5到30之间");

        String validateMessage = messageGenerator.generateConfig("email", validator);
        System.out.println(validateMessage);
    }
}
