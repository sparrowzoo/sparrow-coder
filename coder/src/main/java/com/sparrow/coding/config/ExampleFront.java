package com.sparrow.coding.config;

import com.sparrow.coding.protocol.ControlType;
import com.sparrow.coding.protocol.Entity;
import com.sparrow.coding.protocol.Form;
import com.sparrow.coding.protocol.validate.AllowInputCharLengthValidator;
import com.sparrow.coding.protocol.validate.AllowOptionsValidator;
import com.sparrow.coding.protocol.validate.ChineseCharactersValidator;
import com.sparrow.coding.protocol.validate.DigitalValidator;
import com.sparrow.coding.protocol.validate.EmailValidator;
import com.sparrow.coding.protocol.validate.EqualValidator;
import com.sparrow.coding.protocol.validate.IdCardValidator;
import com.sparrow.coding.protocol.validate.MobileValidator;
import com.sparrow.coding.protocol.validate.NullValidator;
import com.sparrow.coding.protocol.validate.TelValidator;
import com.sparrow.coding.protocol.validate.UserNameRuleValidator;
import com.sparrow.protocol.FieldOrder;

/**
 * 示例代码
 */
@Entity(name = "sparrowExample", text = "用户")
public class ExampleFront {
    @Form(text = "用户ID", primaryKey = true, type = ControlType.INPUT_HIDDEN, listType = ControlType.CHECK_BOX, showInList = true)
    @FieldOrder(order = 1)
    private Long id;

    @Form(text = "用户头象", type = ControlType.IMAGE, listType = ControlType.IMAGE)
    @FieldOrder(order = 1.2F)
    private String avatar;

    @Form(text = "用户名", type = ControlType.INPUT_TEXT, validate = UserNameRuleValidator.class, showInList = true, listType = ControlType.LINK)
    @UserNameRuleValidator(prompt = "请输入6-20个字符(字母、数字或下划线)推荐字母+数字组合的用户名。"
        , nullError = "请输入6-20个字符的用户名")
    @FieldOrder(order = 2)
    private String userName;

    @Form(text = "密码", type = ControlType.INPUT_PASSWORD)
    @NullValidator(prompt = "请输入密码", nullError = "用户密码不允许为空", allowNull = false, minLength = 6, maxLength = 20, lengthError = "密码要求6-20位字符")
    @FieldOrder(order = 3)
    private String password;

    @Form(text = "状态", type = ControlType.ENABLE_DISABLE, validate = AllowOptionsValidator.class, showInList = true)
    @AllowOptionsValidator(options = {"1", "2", "3"}, defaultValue = "1")
    @FieldOrder(order = 4)
    private String status;

    @Form(text = "年龄", type = ControlType.INPUT_TEXT, validate = DigitalValidator.class, showInList = true)
    @DigitalValidator(prompt = "请输入年龄", allowNull = true,
        digitalError = "年龄必须>0并且<100", minValue = 1, maxValue = 100)
    @FieldOrder(order = 5)
    private String age;

    @Form(text = "E-mail", type = ControlType.INPUT_TEXT, validate = EmailValidator.class, showInList = true)
    @EmailValidator(prompt = "请输入邮箱", emailError = "邮箱格式输入错误", minLength = 10, maxLength = 255, lengthError = "邮箱长度必须>=10 并且<255", setError = "邮箱已存在")
    @FieldOrder(order = 6)
    private String email;

    @Form(text = "确认密码", type = ControlType.INPUT_PASSWORD, validate = EqualValidator.class)
    @EqualValidator(prompt = "请输入确认密码",
        otherCtrlId = "txtPassword",
        nullError = "确认密码不能为空",
        noEqualError = "两次密码输入不一致",
        minLength = 6,
        maxLength = 30,
        lengthError = "密码长度必须>=6 并且小于30"
    )
    @FieldOrder(order = 7)
    private String confirmPassword;

    @Form(text = "身份证号码", type = ControlType.INPUT_TEXT, validate = IdCardValidator.class, showInList = true)
    @IdCardValidator(
        prompt = "请输入身份证号码",
        nullError = "身份证号码不允许为空",
        minLength = 18,
        maxLength = 18,
        idCardError = "身份证号码输入有误",
        lengthError = "请输入18位身份证号码",
        allowNull = true)
    @FieldOrder(order = 9)
    private String idCard;

    @Form(text = "手机号", type = ControlType.INPUT_TEXT, validate = MobileValidator.class)
    @MobileValidator
    @FieldOrder(order = 10)
    private String mobile;

    @Form(text = "电话号码", type = ControlType.INPUT_TEXT, validate = TelValidator.class, showInList = true)
    @TelValidator
    @FieldOrder(order = 11)
    private String tel;

    @Form(text = "备注", type = ControlType.TEXT_AREA, validate = AllowInputCharLengthValidator.class)
    @AllowInputCharLengthValidator(maxAllowCharLength = 512, allowCharLengthShowControlId = "spanAllowCharLength")
    @FieldOrder(order = 12)
    private String remark;

    @Form(text = "真实姓名", type = ControlType.INPUT_TEXT, validate = ChineseCharactersValidator.class)
    @ChineseCharactersValidator
    @FieldOrder(order = 13)
    private String name;
}
