package com.sparrow.coding.frontend.validate1;

import com.sparrow.coding.frontend.User;
import com.sparrow.coding.frontend.validate.NullValidatorMessageGenerator;
import com.sparrow.coding.protocol.ControlType;
import com.sparrow.coding.protocol.Form;
import com.sparrow.coding.protocol.validate.EmailValidator;
import com.sparrow.coding.protocol.validate.NullValidator;
import java.lang.reflect.Field;

public class MessageConverterV1 {
    @Form(text = "密码", type = ControlType.INPUT_PASSWORD, validate = NullValidator.class)
    @NullValidator(prompt = "请输入密码", nullError = "用户密码不允许为空", allowNull = false, minLength = 6, maxLength = 20, lengthError = "密码要求6-20位字符")
    private String password;

    /**
     * var appInfo = { txtAppName : { errorCtrlId : 'errorAppName', prompt : '璇疯緭鍏�20浣嶄互涓婣PP鍚嶇О', nullError :
     * 'APP鍚嶇О涓哄繀娣婚」銆�', minLength : 1, maxLength : 20, lengthError : 'APP鍚嶇О闀垮害涓嶅緱澶氫簬20銆�' },
     * <p>
     * txtAppCode : { errorCtrlId : 'errorAppCode', prompt : '璇疯緭鍏�20浣嶄互涓婣PP缂栫爜', nullError : 'APP缂栫爜涓哄繀娣婚」銆�',
     * minLength : 1, maxLength : 20, lengthError : 'APP鍚嶇О闀垮害涓嶅緱澶氫簬20銆�' }, txtAppSort : { errorCtrlId :
     * 'errorAppSort', prompt : '璇疯緭鍏�20浣嶄互涓婣PP缂栫爜', nullError : 'APP缂栫爜涓哄繀娣婚」銆�', digitalError:'鎺掑簭鍙凤紝璇疯緭鍏�0-999涔嬮棿鐨勬暟瀛�',
     * minValue : 0, maxValue : 999, defaultValue : 0 }, txtState : { errorCtrlId : 'errorState', prompt :
     * '璇疯緭鍏ョ姸鎬佺爜0锛氭棤鏁� 1鏈夋晥', options :[1, 0] }, txtRemark : { errorCtrlId : 'errorRemark', prompt : '璇疯緭鍏ヨ缁勭殑鎻忚堪淇℃伅',
     * lengthError : '鎻忚堪淇℃伅涓嶅緱瓒呰繃500涓瓧绗︺€�', spanTxtCount : 'spanTxtCount', allowNull : true } };var appInfo = {
     * txtAppName : { errorCtrlId : 'errorAppName', prompt : '璇疯緭鍏�20浣嶄互涓婣PP鍚嶇О', nullError : 'APP鍚嶇О涓哄繀娣婚」銆�',
     * minLength : 1, maxLength : 20, lengthError : 'APP鍚嶇О闀垮害涓嶅緱澶氫簬20銆�' },
     * <p>
     * txtAppCode : { errorCtrlId : 'errorAppCode', prompt : '璇疯緭鍏�20浣嶄互涓婣PP缂栫爜', nullError : 'APP缂栫爜涓哄繀娣婚」銆�',
     * minLength : 1, maxLength : 20, lengthError : 'APP鍚嶇О闀垮害涓嶅緱澶氫簬20銆�' }, txtAppSort : { errorCtrlId :
     * 'errorAppSort', prompt : '璇疯緭鍏�20浣嶄互涓婣PP缂栫爜', nullError : 'APP缂栫爜涓哄繀娣婚」銆�', digitalError:'鎺掑簭鍙凤紝璇疯緭鍏�0-999涔嬮棿鐨勬暟瀛�',
     * minValue : 0, maxValue : 999, defaultValue : 0 }, txtState : { errorCtrlId : 'errorState', prompt :
     * '璇疯緭鍏ョ姸鎬佺爜0锛氭棤鏁� 1鏈夋晥', options :[1, 0] }, txtRemark : { errorCtrlId : 'errorRemark', prompt : '璇疯緭鍏ヨ缁勭殑鎻忚堪淇℃伅',
     * lengthError : '鎻忚堪淇℃伅涓嶅緱瓒呰繃500涓瓧绗︺€�', spanTxtCount : 'spanTxtCount', allowNull : true } };
     *
     * @param args
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //拿到需要解析的类
        Class userClazz = User.class;
        //通过反射拿到字段
        Field[] userFields = userClazz.getDeclaredFields();
        //定义要返回的json builder
        StringBuilder sb = new StringBuilder();
        //遍历每一个字段
        for (Field field : userFields) {
            //拿到form注解
            Form form = field.getAnnotation(Form.class);
            //拿到fieldName
            String fieldName = field.getName();
            //拿到定义的控件前缀
            String controlPrefix = form.type().getPrefix();
            //拿到定义的验证注解类
            Class annotationClass = form.validate();

            /**
             *     @Form(text = "密码", type = ControlType.INPUT_PASSWORD, validate = NullValidator.class)
             *     @NullValidator(prompt = "请输入密码", nullError = "用户密码不允许为空", allowNull = false, minLength = 6, maxLength = 20, lengthError = "密码要求6-20位字符")
             *     private String password;
             */
            if (annotationClass.equals(NullValidator.class)) {
                NullValidator nullValidator = field.getAnnotation(NullValidator.class);
                String fieldJson = new NullValidatorMessageGenerator().generateValidateMessage(fieldName, controlPrefix, nullValidator);
                sb.append(fieldJson);
            } else if (annotationClass.equals(EmailValidator.class)) {
                EmailValidator emailValidator = field.getAnnotation(EmailValidator.class);
                String fieldJson = new NullValidatorMessageGenerator().generateValidateMessage(fieldName, controlPrefix, emailValidator);
                sb.append(fieldJson);
            }
            System.out.println(sb.toString());
            /**
             * ....
             */
        }
    }
}
