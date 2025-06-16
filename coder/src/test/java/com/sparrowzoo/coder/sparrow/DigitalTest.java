package com.sparrowzoo.coder.sparrow;

import com.sparrow.coding.DigitalCategory;
import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.api.ValidatorRegistry;
import com.sparrow.coding.protocol.validate.DigitalValidator;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import org.junit.jupiter.api.Test;

public class DigitalTest {


    @Test
    public void digital() {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder().initController(false)
                .initInterceptor(false)
                .scanBasePackage("com.sparrow"));
        ValidatorMessageGenerator messageGenerator = ValidatorRegistry.getInstance().getValidatorMessageGenerator("valibot",
                "digitalValidatorMessageGenerator");

        DigitalValidator validator = new DigitalValidator();
        validator.setI18n(true);
        //validator.setI18nFieldName("age");
        validator.setAllowEmpty(false);
        validator.setEmptyMessage("年龄不允许为空");
        validator.setMinValue(5);
        validator.setMaxValue(30);
        validator.setDigitalMessage("请输入正确的年龄");
        validator.setCategory(DigitalCategory.INTEGER);
        validator.setMinValueMessage("年龄不能小于5");
        validator.setMaxValueMessage("年龄不能大于30");

        String validateMessage = messageGenerator.generateConfig("age", validator);
        String i18nValidateMessage = messageGenerator.generateI18NConfig(validator);
        System.out.println(i18nValidateMessage);
        System.out.println(validateMessage);
    }
}
