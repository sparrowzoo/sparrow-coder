package com.sparrow.coding.frontend;

import com.sparrow.coding.config.CoderConfig;
import com.sparrow.coding.frontend.enums.FrontendPlaceholderKey;
import com.sparrow.coding.java.enums.PlaceholderKey;
import com.sparrow.coding.protocol.ControlType;
import com.sparrow.coding.protocol.Entity;
import com.sparrow.coding.protocol.Form;
import com.sparrow.coding.support.utils.ConfigUtils;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrow.utility.Xml;
import com.sparrow.xml.DefaultDocumentLoader;
import com.sparrow.xml.DocumentLoader;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * 代码生成器环境上下文
 * test
 */
public class EnvironmentContext {
    private static Logger logger = LoggerFactory.getLogger(EnvironmentContext.class);
    /**
     * current author
     */
    private String author;
    /**
     * sparrow coder config
     */
    private Properties config;

    public EnvironmentContext(String configPath) throws IOException {
        if (StringUtility.isNullOrEmpty(configPath)) {
            configPath = "default";
        }
        /**
         * config path
         */
        Properties config = ConfigUtils.initPropertyConfig(configPath);
        this.config = config;
        this.author = config.getProperty(CoderConfig.AUTHOR);
        System.out.printf("author is %s\n", this.author);
    }

    public class Config {
        /**
         * 当前对象
         */
        private Class<? extends POJO> clazz;
        /**
         * 前端代码生成路径
         */
        private String frontendGeneratorConfigPath;
        /**
         * js 国际化语言生成路径
         */
        private String languageJsPath;
        /**
         * js 生成路径
         */
        private String jsPath;
        /**
         * css 生成路径
         */
        private String cssPath;
        /**
         * view  模板路径
         */
        private String viewTemplatePath;
        /**
         * 扩展名
         */
        private String extension;
        /**
         * place holder
         */
        private Map<String, String> placeHolder = new HashMap<>();

        /**
         * 字段级别的placeholder
         */
        private Map<String, Map<String, String>> fieldPlaceholders = new HashMap<>();

        private Document document;

        public Config(Class<? extends POJO> clazz) throws ParserConfigurationException, SAXException, IOException {
            this.clazz = clazz;
            this.frontendGeneratorConfigPath = config.getProperty(CoderConfig.FRONTEND_GENERATOR_CONFIG);
            this.languageJsPath = config.getProperty(CoderConfig.LANGUAGE_JS_PATH);
            this.jsPath = config.getProperty(CoderConfig.JS_PATH);
            this.cssPath = config.getProperty(CoderConfig.CSS_PATH);
            this.viewTemplatePath = config.getProperty(CoderConfig.VIEW_TEMPLATE_PATH);
            this.extension = config.getProperty(CoderConfig.EXTENSION);

            this.initPlaceHolder();

            this.viewTemplatePath = StringUtility.replace(this.viewTemplatePath, this.placeHolder);
            this.jsPath = StringUtility.replace(this.jsPath, this.placeHolder);
            this.cssPath = StringUtility.replace(this.cssPath, this.placeHolder);
            this.languageJsPath = StringUtility.replace(this.languageJsPath, this.placeHolder);
            this.frontendGeneratorConfigPath = StringUtility.replace(this.frontendGeneratorConfigPath, this.placeHolder);
            if (StringUtility.isNullOrEmpty(this.frontendGeneratorConfigPath)) {
                this.frontendGeneratorConfigPath = "/sparrow_generator.xml";
            }
            DocumentLoader documentLoader = new DefaultDocumentLoader();
            this.document = documentLoader.loadDocument(this.frontendGeneratorConfigPath, false);
            this.initManagePlaceholder();
        }

        private String replaceSelectAll() {
            Element element = Xml.getElementByTagAttribute(this.document, "manage_page_header_field", "check_box", "true");
            String content = element.getTextContent();
            content = StringUtility.replace(content, this.placeHolder);
            return content;
        }

        private String replaceManageHeaderField(Field field) {
            Element element = Xml.getElementByTagAttribute(this.document, "manage_page_header_field", "check_box", "false");
            String content = element.getTextContent();
            content = StringUtility.replace(content, this.newFieldPlaceholder(field));
            return content;
        }

        private Map<String, String> newFieldPlaceholder(Field field) {
            Map<String, String> fieldPlaceholder = new HashMap<>();
            fieldPlaceholder.putAll(this.placeHolder);
            fieldPlaceholder.putAll(this.fieldPlaceholders.get(field.getName()));
            return fieldPlaceholder;
        }

        private String replaceManageField(Field field) {
            Form form = field.getAnnotation(Form.class);
            Element element = Xml.getElementByTagAttribute(this.document, "manage_page_field", "control_type", form.listType().name());
            String content = element.getTextContent();
            content = StringUtility.replace(content, this.newFieldPlaceholder(field));
            return content;
        }

        private String replaceEditField(Field field) {
            try {
                Form form = field.getAnnotation(Form.class);
                ControlType controlType = form.type();
                Element element = Xml.getElementByTagAttribute(this.document, "create_page_field", "control_type", controlType.name());
                String content = element.getTextContent();
                content = StringUtility.replace(content, this.newFieldPlaceholder(field));
                return content;
            } catch (Exception e) {
                logger.error(" field edit is error {}", field.getName(), e);
                return "<!-- filed generate error " + field.getName() + "-->";
            }
        }

        private String generateEditFields() {
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder editForm = new StringBuilder();
            for (Field field : fields) {
                Form form = field.getAnnotation(Form.class);
                if (form == null) {
                    continue;
                }
                if (form.showInEdit()) {
                    editForm.append(this.replaceEditField(field));
                }
            }
            return editForm.toString();
        }

        private void initManagePlaceholder() {
            StringBuilder manageHeaderLine = new StringBuilder();
            StringBuilder manageDataLine = new StringBuilder();
            manageHeaderLine.append(this.replaceSelectAll());
            manageHeaderLine.append(Constant.ENTER_TEXT);
            List<Field> fieldList = ClassUtility.extractFields(this.clazz);
            Field[] fields = ClassUtility.getOrderedFields(fieldList);
            for (Field field : fields) {
                Form form = field.getAnnotation(Form.class);
                if (form == null) {
                    continue;
                }
                if (form.showInList()) {
                    if (!form.primaryKey()) {
                        manageHeaderLine.append(this.replaceManageHeaderField(field));
                    }
                    manageDataLine.append(this.replaceManageField(field));
                }
            }
            this.placeHolder.put(FrontendPlaceholderKey.$manage_data_table.name(), manageDataLine.toString());
            this.placeHolder.put(FrontendPlaceholderKey.$manage_header_line.name(), manageHeaderLine.toString());
        }

        private void initPlaceHolder() {
            Entity entity = clazz.getAnnotation(Entity.class);
            Map<String, String> context = new HashMap<>();

            this.placeHolder = context;

            String modulePrefix = config.getProperty(CoderConfig.MODULE_PREFIX + "prefix");

            context.put(PlaceholderKey.$module_prefix.name(), modulePrefix);

            String project = config.getProperty(CoderConfig.PROJECT);

            String workspace= config.getProperty(CoderConfig.WORKSPACE);
            String userHome=System.getProperty("user.home");
            workspace=workspace.replace("${user.home}",userHome);

            String resourceWorkspace= config.getProperty(CoderConfig.RESOURCE_WORKSPACE);
            resourceWorkspace=resourceWorkspace.replace("${user.home}",userHome);
            context.put(FrontendPlaceholderKey.$project.name(), project);
            context.put(FrontendPlaceholderKey.$workspace.name(),workspace);
            context.put(FrontendPlaceholderKey.$resource_workspace.name(),resourceWorkspace);
            context.put(FrontendPlaceholderKey.$entity_name.name(), entity.name());
            context.put(FrontendPlaceholderKey.$entity_by_horizontal.name(), StringUtility.humpToLower(entity.name(), '-'));
            context.put(FrontendPlaceholderKey.$entity_by_slash.name(), StringUtility.humpToLower(entity.name(), File.separatorChar));
            context.put(FrontendPlaceholderKey.$upper_entity_name.name(), StringUtility.setFirstByteUpperCase(entity.name()));
            context.put(FrontendPlaceholderKey.$entity_text.name(), entity.text());

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Form form = field.getAnnotation(Form.class);
                if (form == null) {
                    continue;
                }
                if (form.primaryKey()) {
                    context.put(FrontendPlaceholderKey.$primary_key.name(), field.getName());
                }
                Map<String, String> fieldPlaceholder = new HashMap<>();
                fieldPlaceholder.put(FrontendPlaceholderKey.$field_text.name(), form.text());
                fieldPlaceholder.put(FrontendPlaceholderKey.$property_name.name(), field.getName());
                fieldPlaceholder.put(FrontendPlaceholderKey.$upper_property_name.name(), StringUtility.setFirstByteUpperCase(field.getName()));
                fieldPlaceholder.put(FrontendPlaceholderKey.$width.name(), form.width() + "");
                fieldPlaceholders.put(field.getName(), fieldPlaceholder);
            }
        }

        public void generateCreatePage() {
            Element headerElement = Xml.getElementByTagAttribute(this.document, "create_page", "header_tail", "HEADER");
            String createPageHeaderContent = StringUtility.replace(headerElement.getTextContent(), this.placeHolder);

            Element tailElement = Xml.getElementByTagAttribute(this.document, "create_page", "header_tail", "TAIL");
            String createPageTailContent = StringUtility.replace(tailElement.getTextContent(), this.placeHolder);

            String editFields = this.generateEditFields();

            String content = createPageHeaderContent + editFields + createPageTailContent;
            System.out.println(content);

            String entityName = placeHolder.get(FrontendPlaceholderKey.$entity_name.name());
            String fullPath = this.viewTemplatePath + File.separator + StringUtility.humpToLower(entityName, File.separatorChar) + File.separator + "new" + this.extension;
            FileUtility.getInstance().writeFile(fullPath, content);
        }

        public void generateManagePage() {
            Element headerElement = Xml.getElementByTagAttribute(this.document, "manage_page", "header_tail", "HEADER");
            String managePageHeaderContent = StringUtility.replace(headerElement.getTextContent(), this.placeHolder);

            Element tailElement = Xml.getElementByTagAttribute(this.document, "manage_page", "header_tail", "TAIL");
            String managePageTailContent = StringUtility.replace(tailElement.getTextContent(), this.placeHolder);
            String content = managePageHeaderContent + managePageTailContent;
            System.out.println(content);
            String entityName = placeHolder.get(FrontendPlaceholderKey.$entity_by_slash.name());
            String fullPath = this.viewTemplatePath + File.separator + entityName + File.separator + "manage" + this.extension;
            System.out.println("write to path:" + fullPath);
            FileUtility.getInstance().writeFile(fullPath, content);
        }

        public void generateCreateJs() {
            String content = Xml.getElementTextContent(this.document, "create_page_js");
            content = StringUtility.replace(content, this.placeHolder);
            System.out.println(content);
            String entityName = placeHolder.get(FrontendPlaceholderKey.$entity_by_slash.name());
            String fullPath = this.jsPath + File.separator + entityName + File.separator + "new.js";
            System.out.println("write to path:" + fullPath);
            FileUtility.getInstance().writeFile(fullPath, content);
        }

        public void generateManageJs() {
            String content = Xml.getElementTextContent(this.document, "manage_page_js");
            content = StringUtility.replace(content, this.placeHolder);
            System.out.println(content);
            String entityName = placeHolder.get(FrontendPlaceholderKey.$entity_by_slash.name());
            String fullPath = this.jsPath + File.separator + entityName + File.separator + "manage.js";
            System.out.println("write to path:" + fullPath);
            FileUtility.getInstance().writeFile(fullPath, content);
        }

        public void generateLanguageJs() {
            String content = Xml.getElementTextContent(this.document, "language_js");
            content = StringUtility.replace(content, this.placeHolder);
            List<Field> fieldList = ClassUtility.extractFields(this.clazz);
            Field[] fields = ClassUtility.getOrderedFields(fieldList);
            String entityName = this.placeHolder.get(FrontendPlaceholderKey.$entity_name.name());
            StringBuilder sb = new StringBuilder(entityName + "Info={");
            for (Field field : fields) {
                try {
                    Form form = field.getAnnotation(Form.class);
                    Annotation validator = field.getAnnotation(form.validate());
                    if (validator == null) {
                        logger.error("field:{} validate is null ", field.getName());
                        continue;
                    }
                    String fieldJson = ValidatorRegistry.getInstance().getValidatorMessageGenerator(validator.annotationType())
                        .generateValidateMessage(field.getName(), form.type().getPrefix(), validator);
                    sb.append(fieldJson);
                    sb.append(",");
                } catch (Exception e) {
                    logger.error("field:{} validate generate error ", field.getName(), e);
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("}");
            content = content + sb.toString();
            System.out.println(content);
            String fullPath = this.languageJsPath + File.separator + this.placeHolder.get(FrontendPlaceholderKey.$entity_by_horizontal.name()) + ".js";
            System.out.println("write to path:" + fullPath);
            FileUtility.getInstance().writeFile(fullPath, content);
        }
    }
}