package com.sparrow.coding.config;

import com.sparrow.coding.support.enums.ValidateType;

import java.util.HashMap;
import java.util.Map;


public class CoderConfig {
	public static String AUTHOR="author";
	public static String FULL_TABLE_NAME="full_table_name";
	public static String RESOURCE_WORKSPACE="resource_workspace";
	public static String WORKSPACE="workspace";

	public static String LANGUAGE_JS_PATH = "language_js_path";
	public static String JS_PATH = "js_path";
	public static String VIEW_TEMPLATE = "view_template";
	public static String CSS_PATH = "css_path";

	public static String MODULE_PREFIX="module.";
	public static String PACKAGE_PREFIX="package.";
	public static String CLASS_PREFIX="class.";

	public static String MODULE_PARENT_ADMIN="parent.admin";
	public static String PROJECT="project";






	public static Map<String, Map<ValidateType, String>> validate_error_msg = new HashMap<String, Map<ValidateType, String>>() {
		private static final long serialVersionUID = 1L;
		{
			this.put("zh_cn", new HashMap<ValidateType, String>() {
				private static final long serialVersionUID = 1L;
				{
					this.put(ValidateType.DIGITAL, "只允许输入数字");				this.put(ValidateType.EMAIL, "email格式错误");
					this.put(ValidateType.EQUAL, "与$compareTo 不一致,请重新输入");
					this.put(ValidateType.ID_CARD, "身份证格式错误");
					this.put(ValidateType.MOBILE, "手机号码格式错误");
					this.put(ValidateType.NAME_ROLE,
							"请输入6-20个字符字母、数字或下划线<br/>推荐字母+数字组合的用户名。");
					this.put(ValidateType.NULL, "$displayNasme不允许为空");
					this.put(ValidateType.TELEPHONE, "电话号码格式错误");
					this.put(ValidateType.WORD, "请输入汉字");
				}
			});
		}
	};

	public static Map<String, Map<ValidateType, String>> validate_prompt_msg = new HashMap<String, Map<ValidateType, String>>() {

		private static final long serialVersionUID = 1L;

		{
			this.put("zh_cn", new HashMap<ValidateType, String>() {

				private static final long serialVersionUID = 1L;
				{
					this.put(ValidateType.DIGITAL, "请输入数字 ");
					this.put(ValidateType.EMAIL, "请输入email");
					this.put(ValidateType.EQUAL, "请输入$diaplayName");
					this.put(ValidateType.ID_CARD, "请输入身份证号码");
					this.put(ValidateType.MOBILE, "请输入11位手机号码 例:13581571234");
					this.put(ValidateType.NAME_ROLE,
							"请输入6-20个字符字母、数字或下划线<br/>推荐字母+数字组合的用户名。例:zh_2014");
					this.put(ValidateType.NULL, "请输入$displayName");
					this.put(ValidateType.TELEPHONE, "请输入电话号码 例:010-69107890");
					this.put(ValidateType.WORD, "请输入汉字");
					this.put(ValidateType.ALLOW_INPUT_OPTION,
							"请按指定选项输入[0:无效,1有效]");
				}
			});
		}
	};

	public static Map<ValidateType, String> validate_field = new HashMap<ValidateType, String>() {
		private static final long serialVersionUID = 1L;
		{
			this.put(ValidateType.EMAIL, "emailError");
			this.put(ValidateType.EQUAL, "noEqualError");
			this.put(ValidateType.ID_CARD, "idCardError");
			this.put(ValidateType.MOBILE, "mobileError");
			this.put(ValidateType.NAME_ROLE, "nameRuleError");
			this.put(ValidateType.NULL, "nullError");
			this.put(ValidateType.TELEPHONE, "telError");
			this.put(ValidateType.WORD, "wordError");
			this.put(ValidateType.ALLOW_INPUT_OPTION, "");
			this.put(ValidateType.DIGITAL, "digitalError");
		}
	};
}
