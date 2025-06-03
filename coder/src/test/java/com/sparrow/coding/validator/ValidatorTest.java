package com.sparrow.coding.validator;

import com.sparrow.coding.ApplicationBoot;
import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.api.ValidatorRegistry;
import com.sparrow.coding.frontend.validate.valibot.StringValidatorMessageGenerator;
import com.sparrow.coding.protocol.validate.EmailValidator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationBoot.class})
public class ValidatorTest {
    @Test
    public void test() {
        ValidatorMessageGenerator messageGenerator = ValidatorRegistry.getInstance().getValidatorMessageGenerator("valibot",
                StringValidatorMessageGenerator.class);

        EmailValidator validator = new EmailValidator();
        validator.setI18n(true);
        validator.setAllowEmpty(true);
        validator.setEmptyMessage("用户名不能为空");
        validator.setMinLength(5);
        validator.setMaxLength(30);
        validator.setFormatMessage("请输入正确的邮箱格式");
        validator.setLengthMessage("用户名长度必须在5到30之间");

        String validateMessage = messageGenerator.generateValidateMessage("email", validator);
        System.out.println(validateMessage);
    }
}
