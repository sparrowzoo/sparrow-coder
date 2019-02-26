//package com.sparrow.coding;
//
//import com.sparrow.coding.config.CONFIG;
//import com.sparrow.coding.config.EnvironmentContext;
//import com.sparrow.coding.entity.Field;
//import com.sparrow.coding.entity.Table;
//import com.sparrow.coding.support.enums.FIELD_CONTROL_TYPE;
//import com.sparrow.coding.support.enums.REPLACE_KEY;
//import com.sparrow.coding.support.enums.VALIDATE_TYPE;
//import com.sparrow.coding.support.utils.GenerateUtil;
//import com.sparrow.core.spi.ApplicationContext;
//import com.sparrow.datasource.DataSourceFactory;
//import com.sparrow.datasource.DatasourceConfig;
//import com.sparrow.support.EnvironmentSupport;
//import com.sparrow.utility.FileUtility;
//import com.sparrow.utility.StringUtility;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class GeneratePage {
//
//    private Map<String, String> replaceMap = null;
//
//    private StringBuffer loadBefore = new StringBuffer();
//    private StringBuffer load = new StringBuffer();
//    private StringBuffer loadAfter = new StringBuffer();
//
//    private String manageHiddenId = null;
//    private String grvId = null;
//
//    private String lowerTableName = null;
//
//    private static GeneratePage g = null;
//
//    private String moduleName = "新媒体类别Id";
//    private String idFieldName = "MediaTypeId";
//
//    private EnvironmentContext environmentIO;
//
//    private void initReplaceMap() {
//        this.replaceMap = new HashMap<String, String>();
//    }
//
//    public static void main(String[] args) {
//        Class templateClass = null;//MediaTypeTemplate.class;
//        ApplicationContext.getContainer().init();
//
//        //DataSourceFactory dataSourceFactory=new DataSourceFactory("sparrow_default");
//        System.out.println(EnvironmentSupport.getInstance().getWorkspace());
//        g = new GeneratePage();
//        //DatasourceConfig datasourceConfig= new DataSourceFactory().getDatasourceConfig();
//        //g.environmentIO=new EnvironmentContext(datasourceConfig.getDatabaseName());
//
//        g.writeNewPage(templateClass);
//        g.writeManagePage(templateClass);
//        g.writeNewJs();
//    }
//
//    public void writeManagePage(Class<?> clazz) {
//        List<Field> fieldList = g.getFieldList(clazz);
//        Table table = clazz.getAnnotation(Table.class);
//        String tableName = StringUtility.setFirstByteLowerCase(table.tableName());
//        this.grvId = "grv" + StringUtility.setFirstByteUpperCase(tableName) + "List";
//        String startPage = environmentIO.readManageStart();
//        startPage = startPage.replace(REPLACE_KEY.$lower_table_name.toString(),
//                table.lowerTableName());
//        startPage = startPage.replace(REPLACE_KEY.$table_name.toString(),
//                table.tableName());
//
//        String endPage = environmentIO.readManageEnd();
//        StringBuffer fields = new StringBuffer();
//        StringBuffer headTitles = new StringBuffer();
//        String idField = null;
//
//        StringBuffer condition = new StringBuffer();
//        for (Field f : fieldList) {
//            if (f.showInList()) {
//                fields.append(g.getListLineByField(f) + "\n");
//                if (f.displayOrder() == 1) {
//                    headTitles.append("checkBox");
//                    idField = String
//                            .format("<J:JHidden name=\"%1$s\" id=\"hdn%2$s\"/>",
//
//                                    StringUtility.setFirstByteLowerCase(f.fieldName()),
//
//                                    StringUtility.setFirstByteUpperCase(f.fieldName()));
//
//                } else {
//                    if(f.displayName().equals("control_text_edit")){
//                        headTitles.append("编辑");
//                    }
//                    else
//                    {
//                        headTitles.append(f.displayName());
//                    }
//                    if (!f.controlType().equals(FIELD_CONTROL_TYPE.Button) && !f.controlType().equals(FIELD_CONTROL_TYPE.Hidden)) {
//                        condition.append(f.displayName() + ":"
//                                + this.getConditionInputByField(f));
//                    }
//                }
//
//                headTitles.append("|");
//            }
//        }
//
//        startPage = startPage.replace("$idField", idField);
//        startPage = startPage.replace("$condition", condition.toString());
//
//
//        String gridViewContent = "<J:JGridView indent=\"5\" name=\""
//                + tableName + "List\" id=\"" + this.grvId + "\""
//                + " cssText=\"width:860px;\""
//                + " headTitles=\"" + headTitles.toString()
//                + "\" showPage=\"true\"\n" + "\nfields=\n\""
//                + fields.toString() + "\"/>";
//        String pageContent = startPage + gridViewContent + endPage;
//        GenerateUtil.writeManagePage(tableName, pageContent);
//    }
//
//    private void writeNewPage(Class<?> clazz) {
//        List<Field> fieldList = g.getFieldList(clazz);
//        Table table = clazz.getAnnotation(Table.class);
//        String tableName = table.tableName();
//        this.lowerTableName = StringUtility.setFirstByteLowerCase(tableName);
//        String startPage = environmentIO.readNewStart();
//        String endPage = environmentIO.readNewEnd();
//        StringBuffer pager = new StringBuffer();
//        for (Field f : fieldList) {
//            if (f.showInAdd()) {
//                pager.append(g.getEditLineByField(f));
//            }
//        }
//        String newPageContent = startPage + pager.toString() + endPage;
//        newPageContent = newPageContent.replace(
//                REPLACE_KEY.$table_name.toString(), lowerTableName);
//        GenerateUtil.writeNewPage(lowerTableName, newPageContent);
//        this.writeCss();
//        this.writeJavascript(clazz);
//    }
//
//    private void writeCss() {
//        // 写css
//        String rootPath = EnvironmentSupport.getInstance()
//                .getApplicationSourcePath();
//        String newcss = FileUtility.getInstance().readFileContent(
//                rootPath + "/src/main/resources/template/newcss.txt");
//
//        newcss = newcss.replace("$tableName", lowerTableName);
//
//        String cssPath = environmentIO.getResourceFullPath(CONFIG.CSS_PATH);
//        FileUtility.getInstance().writeFile(
//                cssPath + "/" + lowerTableName + ".css", newcss);
//    }
//
//    private void writeNewJs() {
//        String js = this.loadBefore.toString();
//
//        if (this.load.length() > 0) {
//            js += "document.ready(function(){" + this.load.toString() + "});";
//        }
//        js += this.loadAfter.toString();
//        // 写new js
//        String jsPath = environmentIO.getResourceFullPath(CONFIG.JS_PATH);
//        FileUtility.getInstance().writeFile(
//                jsPath + "/" + lowerTableName + "/new.js", js);
//    }
//
//
//    private void writeJavascript(Class<?> clazz) {
//        List<Field> fieldList = g.getFieldList(clazz);
//        Table table = clazz.getAnnotation(Table.class);
//        StringBuffer languageJs = new StringBuffer();
//        languageJs.append(String.format("l = {\n" +
//                "\tmessage: {\n" +
//                "\t\tdel: \"%1$s删除后将无法恢复，您确认要删除吗?\",\n" +
//                "\t\tdeleteFile: \"确定后此%1$s将被彻底删除，您确认要继续本次操作吗？\",\n" +
//                "\t\tupdate: \"%1$s更新成功\",\n" +
//                "\t\tnoSelectRecord: \"请选择%1$s\",\n" +
//                "\t\tenable: \"您确认启用该%1$s吗？\",\n" +
//                "\t\tdisable: \"您确认禁用该%1$s吗?\",\n" +
//                "\t\terror: \"网络繁忙，请稍侯再试。\",\n" +
//                "\t\tresourceName: \"资源版块\",\n" +
//                "\t\taccessDenied:\"访问被拒绝\"\n" +
//                "\t}\n" +
//                "};", moduleName));
//        StringBuffer js = new StringBuffer();
//        languageJs.append(table.lowerTableName() + "Info={\n");
//        int index = 0;
//        for (Field f : fieldList) {
//            if (f.validateType() == null || f.controlType().equals(FIELD_CONTROL_TYPE.Hidden)) {
//                continue;
//            }
//            if (!f.showInAdd()) {
//                continue;
//            }
//            if (index > 0) {
//                languageJs.append(",");
//            }
//            languageJs.append(f.controlType().getPrefix()
//                    + StringUtility.setFirstByteUpperCase(f.fieldName()) + ":{\n");
//            languageJs.append(this.getValidateJavascript(f));
//            languageJs.append("}");
//            index++;
//
//            if (!f.nullable()) {
//                if (js.length() > 0) {
//                    js.append(",");
//                }
//                js.append(f.lowerTableName() + "Info."
//                        + f.controlType().getPrefix()
//                        + StringUtility.setFirstByteUpperCase(f.fieldName()));
//            }
//        }
//        languageJs.append("};");
//        this.loadAfter.append(js.toString());
//
//        String jsPath = environmentIO.getResourceFullPath(CONFIG.LANGUAGE_JS_PATH);
//        FileUtility.getInstance().writeFile(jsPath + "/" + lowerTableName + ".js",
//                languageJs.toString());
//    }
//
//    private List<Field> getFieldList(Class<?> clazz) {
//        java.lang.reflect.Field filed[] = clazz.getFields();
//        List<Field> fieldArray = new ArrayList<Field>();
//        for (java.lang.reflect.Field f : filed) {
//            fieldArray.add(f.getAnnotation(Field.class));
//        }
//        return fieldArray;
//    }
//
//    private String getEditLineByField(Field field) {
//        if (!field.controlType().equals(FIELD_CONTROL_TYPE.Button)) {
//            if (!field.controlType().equals(FIELD_CONTROL_TYPE.Hidden)) {
//                String edit;
//                if (field.controlType().equals(FIELD_CONTROL_TYPE.Editor)) {
//                    edit = "<tr><th>$displayName</th><td colspan=\"2\" class=\"input\">$input</td></tr>";
//                } else {
//                    edit = "<tr><th>$displayName</th><td class=\"input\">$input</td><td class=\"error\"><span class=\"prompt\" id=\"error$fieldName\"></span></td></tr>";
//                }
//                edit = edit.replace("$displayName", field.displayName());
//                edit = edit.replace("$input", this.getInputByField(field));
//                edit = edit.replace("$fieldName", field.fieldName());
//                return edit;
//            } else {
//                if (field.showInAdd()) {
//                    return this.getInputByField(field);
//                } else {
//                    return "";
//                }
//            }
//        } else {
//            return "";
//        }
//    }
//
//    private String getListLineByField(Field filed) {
//        String filedName = StringUtility.setFirstByteLowerCase(filed.fieldName());
//        //hyperLink#name&id#format#url#target#textLength#defaultValue
//        if (filed.isHyperLink()) {
//            return "hyperLink#" + filedName + "&" + idFieldName + "#{0}#/view-{1}-1.jsp#_blank|"
//                    + filed.width() + "px$left|";
//            //checkBox#userId#grvAdministratorList$20px$left|
//        } else if (filed.controlType().equals(FIELD_CONTROL_TYPE.Checkbox) || filed.controlType().equals(FIELD_CONTROL_TYPE.Hidden)) {
//            if (filed.displayOrder() == 1) {
//                return "checkBox#" + filedName + "#" + this.grvId + "$"
//                        + filed.width() + "px$center|";
//            } else {
//                return "text#" + filedName + "$" + filed.width()
//                        + "px$center|";
//            }
//            //text#filedName#format#textLength#defaultValue
//        } else if (filed.controlType().equals(FIELD_CONTROL_TYPE.TextField)) {
//            if(filed.fieldName().toLowerCase().equals("status")){
//                return "text#" + filedName + "#enum:STATUS_RECORD$" + filed.width()
//                        + "px$center|";
//            }
//            else if(filed.fieldName().toLowerCase().contains("time")){
//                return "text#" + filedName + "#date:yyyy-MM-dd HH:mm:ss$" + filed.width()
//                        + "px$center|";
//            }
//            else {
//                return "text#" + filedName + "$" + filed.width()
//                        + "px$center|";
//            }
//            //image#fielName#format#width#height#defaultIco
//            //image#filedName#图片显示文本 title#60px#40px#http://r.**.net/defualt.ico
//        } else if (filed.isImage()) {
//            return "image#" + filedName + "#{0}#"
//                    + filed.width() + "#" + filed.height()
//                    + "$" + filed.width() + "px$left|";
//
//            //button#fieldName&fieldName2#format#javascriptClickEvent({0},{1})#cssClass#
//        } else if (filed.controlType().equals(FIELD_CONTROL_TYPE.Button)) {
//            return "button#" + filedName + "#"
//                    + filed.displayName() + "#window.location.href='" + filed.actionUrl() + "?" + StringUtility.setFirstByteLowerCase(filed.fieldName())
//                    + "={0}';$80px$center";
//        } else {
//            return "text#" + filedName + "$" + filed.width()
//                    + "px$center|";
//        }
//    }
//
//    private String getInputByField(Field field) {
//        switch (field.controlType()) {
//            case Checkbox:
//                return String
//                        .format("<J:JCheckBox name=\"%1$s.%2$s\" id=\"%5$s%3$s\" tabIndex=\"%4$s\"/>",
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.displayOrder(),
//
//                                field.controlType().getPrefix());
//            case Date:
//                return String
//                        .format("<J:JTextBox name=\"%1$s.%2$s\" id=\"%9$s%3$s\" cssText=\"width:%4$s;height:%5$s;\" tabIndex=\"%6$s\" maxLength=\"%7$s\" events=\"%8$s\"/>",
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.width(),
//
//                                field.height(),
//
//                                field.displayOrder(),
//
//                                field.maxLength(),
//
//                                this.getValidatorByField(field),
//
//                                field.controlType().getPrefix());
//
//            case DateHHMMSS:
//                break;
//            case Editor:
//                this.loadBefore.append(String.format("var %1$sEditor =new SparrowEditor(\"%1$sEditor\");",
//                        StringUtility.setFirstByteLowerCase(field.fieldName())));
//                this.load
//                        .append(String
//                                .format(" %1$sEditor.config.tool.adjust.adjustable = false;"
//                                                + "%1$sEditor.config.tool.convertHTML.isConvert = false;"
//                                                + "%1$sEditor.config.style = \"comment\";"
//                                                + "%1$sEditor.initialize(\"div%2$sEditor\");"
//                                                + "%1$sEditor.attach.setParentObject(%1$sEditor);"
//                                                + "%1$sEditor.config.attach.fileInfoId = \"hdnFileInfo\";"
//                                                + "%1$sEditor.attach.validate = function() {$(\"btnSubmit\").disabled = true;\n" +
//                                                " return v.getValidateResult(" + field.lowerTableName() + "Info,false);" +
//                                                "};" +
//                                                " contentEditor.initImageUploadEvent();",
//                                        StringUtility.setFirstByteLowerCase(field
//                                                .fieldName()), StringUtility.setFirstByteUpperCase(field.fieldName())));
//
//                return String
//                        .format("<div class=\"editor\" id=\"div%6$sEditor\""
//                                        + "style=\"width: %2$spx; height: %3$spx;margin-top:8px;\"></div>"
//                                        + "您已经输入 <span id=\"spanWordCount\">0</span>/%7$s字"
//                                        + "<J:JHidden name=\"%4$s.%5$s\" id=\"hdn%6$s\"/>"
//                                        + "<J:JHidden name=\"%4$s.attach\" id=\"hdnFileInfo\"/>",
//                                StringUtility.setFirstByteUpperCase(field.lowerTableName()),
//
//                                field.width(),
//
//                                field.height(),
//
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.maxLength());
//
//            case File:
//                this.load
//                        .append(String.format("file.initCoverImageEvent('%1$s');",
//                                StringUtility.setFirstByteUpperCase(field.fieldName())));
//
//                if (field.isImage()) {
//                    return String
//                            .format("<div id=\"img%1$s\"></div>"
//                                            + "<iframe id=\"fileUpload\" class=\"file-frame\" frameborder=\"0\" src=\"${rootPath}/FileUpload?pathKey=%2$s\"></iframe>",
//                                    StringUtility.setFirstByteLowerCase(field
//                                            .fieldName()), field.pathKey());
//                }
//                break;
//            case Hidden:
//                return String
//                        .format("<J:JHidden name=\"%1$s.%2$s\" id=\"%4$s%3$s\"/>",
//
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.controlType().getPrefix());
//
//            case MultiSelect:
//                break;
//            case Password:
//                break;
//            case Radio:
//                break;
//            case Select:
//
//                return String
//                        .format("<J:JDropDownList enums=\"%4$s\" name=\"%1$s.%2$s\" id=\"%4$s%3$s\"/>",
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.dropDownListEnum().getClass().getName(),
//
//                                field.controlType().getPrefix());
//
//            case TextArea:
//                return String
//                        .format("<J:JTextArea name=\"%1$s.%2$s\" id=\"%9$s%3$s\" cssText=\"width:%4$s;height:%5$s;\" tabIndex=\"%6$s\" events=\"%8$s\"></J:JTextArea><br /> 您还可以输入： <J:JSpan id=\"spanTxtCount\"></J:JSpan> 个字符/%7$s",
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.width(),
//
//                                field.height(),
//
//                                field.displayOrder(),
//
//                                field.maxLength(),
//
//                                this.getValidatorByField(field),
//
//                                field.controlType().getPrefix());
//            case TextField:
//                return String
//                        .format("<J:JTextBox name=\"%1$s.%2$s\" id=\"%9$s%3$s\" cssText=\"width:%4$s;height:%5$s;\" tabIndex=\"%6$s\" %7$s events=\"%8$s\"/>",
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.width(),
//
//                                field.height(),
//
//                                field.displayOrder(),
//
//                                field.maxLength() > 0 ? "maxLength="
//                                        + field.maxLength() : "",
//
//                                this.getValidatorByField(field),
//
//                                field.controlType().getPrefix());
//
//            case Code:
//                String txtControlId = field.controlType().getPrefix()
//                        + StringUtility.setFirstByteUpperCase(field.fieldName());
//                this.loadBefore.append(String.format("var %1$sTree = null;",
//                        StringUtility.setFirstByteLowerCase(field.fieldName())));
//
//                String codeScript = String
//                        .format("%1$sTree = new iTree(\"%1$sTree\");"
//                                        + "%1$sTree.config.floatTreeId = \"div%2$sTree\";"
//                                        + "%1$sTree.config.descTextBoxId =\""
//                                        + txtControlId
//                                        + "\";"
//                                        + "%1$sTree.config.descHiddenId=\""
//                                        + txtControlId.replace("txt", "hdn")
//                                        + "\";"
//                                        + "%1$sTree.config.validate=function(){"
//                                        + "v.isNull(%3$sInfo.txt"
//                                        + StringUtility.setFirstByteUpperCase(field
//                                        .fieldName())
//                                        + ");"
//                                        + "};"
//                                        + "%1$sTree.codeNodeCallBack = function(icodeEntity) {"
//                                        + "};" + "%1$sTree.initCodeToolip('GROUP-');",
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//                                field.fieldName(), field.lowerTableName());
//
//                this.load.append(codeScript);
//
//                return String
//                        .format("<J:JTextBox name=\"%1$s.%10$sName\" id=\"txt%3$s\" cssText=\"width:%4$s;height:%5$s;\" tabIndex=\"%6$s\" %7$s events=\"%8$s\"/><J:JHidden id=\"hdn%3$s\" name=\"%1$s.%10$s\"/>",
//                                field.entityName(),
//
//                                field.lowerTableName(),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.width(),
//
//                                field.height(),
//
//                                field.displayOrder(),
//
//                                field.maxLength() > 0 ? "maxLength="
//                                        + field.maxLength() : "",
//
//                                this.getValidatorByField(field),
//
//                                field.controlType().getPrefix(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()));
//
//            case CheckBoxList:
//
//                return String
//                        .format("<J:JCheckBoxList showTable=\"false\" name=\"%1$s.%2$s\" id=\"%4$s%3$s\"></J:JCheckBoxList>",
//
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.controlType().getPrefix());
//
//            case RadioBoxList:
//
//                return String
//                        .format("<J:JRadioBoxList showTable=\"false\" name=\"%1$s.%2$s\" id=\"%4$s%3$s\"></J:JRadioBoxList>",
//
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.controlType().getPrefix());
//            default:
//                break;
//
//        }
//        return "";
//    }
//
//    private String getConditionInputByField(Field field) {
//        switch (field.controlType()) {
//            case Hidden:
//                return String
//                        .format("<J:JHidden name=\"%1$s.%2$s\" id=\"%4$s%3$s\"/>",
//
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.controlType().getPrefix());
//            case Checkbox:
//                return String
//                        .format("<J:JCheckBox name=\"%1$s.%2$s\" id=\"%5$s%3$s\" tabIndex=\"%4$s\"/>",
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.displayOrder(),
//
//                                field.controlType().getPrefix());
//            case Date:
//
//                this.loadBefore.append(String.format("var %1$sDatePicker =null;",
//                        StringUtility.setFirstByteLowerCase(field.fieldName())));
//                this.load
//                        .append(String.format(
//                                " %1$sDatePicker=new DatePicker(\"%1$sDatePicker\");%1$sDatePicker.config.srcElement=$(\"txt%2$s\");"
//                                        + "%1$sDatePicker.init();",
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//                                StringUtility.setFirstByteUpperCase(field.fieldName())));
//
//                return String
//                        .format("<J:JTextBox name=\"%1$s.%2$s\" id=\"%8$s%3$s\" cssText=\"width:%4$s;height:%5$s;\" tabIndex=\"%6$s\" events=\"%7$s\"></J:JTextBox>",
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.width(),
//
//                                field.height(),
//
//                                field.displayOrder(),
//
//                                this.getValidatorByField(field),
//
//                                field.controlType().getPrefix());
//
//            case DateHHMMSS:
//                break;
//            case Select:
//
//                return String
//                        .format("<J:JDropDownList enums=\"%4$s\" name=\"%1$s.%2$s\" id=\"%4$s%3$s\"></J:JDropDownList>",
//
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.dropDownListEnum().getClass().getName(),
//
//                                field.controlType().getPrefix());
//
//            case TextArea:
//                return String
//                        .format("<J:JTextArea name=\"%1$s.%2$s\" id=\"%8$s%3$s\" cssText=\"width:%4$s;height:%5$s;\" tabIndex=\"%6$s\"></J:JTextArea>",
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.width(),
//
//                                field.height(),
//
//                                field.displayOrder(),
//
//                                field.maxLength(),
//
//                                field.controlType().getPrefix());
//            case TextField:
//                return String
//                        .format("<J:JTextBox name=\"%1$s.%2$s\" id=\"%7$s%3$s\" cssText=\"width:%4$s;height:%5$s;\" tabIndex=\"%6$s\"/>",
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.width(),
//
//                                field.height(),
//
//                                field.displayOrder(),
//
//                                field.controlType().getPrefix());
//            case CheckBoxList:
//
//                return String
//                        .format("<J:JCheckBoxList showTable=\"false\" name=\"%1$s.%2$s\" id=\"%4$s%3$s\"></J:JCheckBoxList>",
//
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.controlType().getPrefix());
//
//            case RadioBoxList:
//
//                return String
//                        .format("<J:JRadioBoxList showTable=\"false\" name=\"%1$s.%2$s\" id=\"%4$s%3$s\"></J:JRadioBoxList>",
//
//                                field.entityName(),
//
//                                StringUtility.setFirstByteLowerCase(field.fieldName()),
//
//                                StringUtility.setFirstByteUpperCase(field.fieldName()),
//
//                                field.controlType().getPrefix());
//            default:
//                break;
//
//        }
//        return null;
//    }
//
//    private String getValidatorByField(Field field) {
//
//        String fieldName = field.controlType().getPrefix()
//                + StringUtility.setFirstByteUpperCase(field.fieldName());
//
//        String validateInfo = StringUtility.setFirstByteLowerCase(field
//                .lowerTableName()) + "Info." + fieldName;
//
//        String result = null;
//
//        switch (field.validateType()) {
//            case DIGITAL:
//                result = "onfocus='v.showMessage(%1$s);'onblur='v.isDigital(%1$s);'%2$s";
//                break;
//            case EMAIL:
//                result = "onfocus='v.showMessage(%1$s);'onblur='v.isEmail(%1$s);'%2$s";
//                break;
//            case EQUAL:
//                result = "onfocus='v.showMessage(%1$s);'onblur='v.isEquel(%1$s);'%2$s";
//                break;
//            case ID_CARD:
//                result = "onfocus='v.showMessage(%1$s);'onblur='v.isIdCard(%1$s);'%2$s";
//                break;
//            case MOBILE:
//                result = "onfocus='v.showMessage(%1$s);'onblur='v.isMobile(%1$s);'%2$s";
//                break;
//            case NAME_ROLE:
//                break;
//            case NULL:
//                result = "onfocus='v.showMessage(%1$s);'onblur='v.isNull(%1$s);'%2$s";
//                break;
//            case TELEPHONE:
//                result = "onfocus='v.showMessage(%1$s);'onblur='v.isTel(%1$s);'%2$s";
//                break;
//            case WORD:
//                result = "onfocus='v.showMessage(%1$s);'onblur='v.isWord(%1$s);'%2$s";
//                break;
//            case ALLOW_INPUT_OPTION:
//                result = "onfocus='v.showMessage(%1$s);'onblur='v.allowInputOption(%1$s);'%2$s";
//                break;
//            case IS_IMG_SIZE:
//                result = "onfocus='v.showMessage(%1$s);'onblur='v.isImgSize(%1$s);'%2$s";
//                break;
//            case IS_FILE_LENGTH:
//                result = "onfocus='v.showMessage(%1$s);'onblur='v.isFileLength(%1$s);'%2$s";
//                break;
//            default:
//                break;
//        }
//        String event = "";
//        if (field.controlType().equals(FIELD_CONTROL_TYPE.Code)) {
//            event = "onclick='"
//                    + StringUtility.setFirstByteLowerCase(field.fieldName())
//                    + "Tree.Show(event,200,350);'";
//        }
//        return String.format(result, validateInfo, event);
//    }
//
//    private String getValidateJavascript(Field field) {
//        if (field.controlType().equals(FIELD_CONTROL_TYPE.Button)) {
//            return "";
//        }
//        StringBuffer sb = new StringBuffer();
//        sb.append("ctrlId : '" + field.controlType().getPrefix()
//                + StringUtility.setFirstByteUpperCase(field.fieldName()) + "',\n");
//
//        sb.append("errorCtrlId : 'error"
//                + StringUtility.setFirstByteUpperCase(field.fieldName()) + "',\n");
//
//        String pmsg = CONFIG.validate_prompt_msg.get("zh_cn").get(
//                field.validateType());
//        pmsg = pmsg.replace("$diaplayName", field.displayName());
//        sb.append("prompt : '" + pmsg + "',\n");
//
//        if (field.minLength() > 0 && field.maxLength() > field.minLength()) {
//            sb.append("minLength :" + field.minLength() + ",\n");
//            sb.append("maxLength :" + field.maxLength() + ",\n");
//            sb.append("lengthError : '" + field.displayName() + "长度只允许在,\n"
//                    + field.minLength() + "-" + field.maxLength() + "个字符之间'\n");
//        }
//        if (field.validateType().equals(VALIDATE_TYPE.DIGITAL)) {
//            if (field.minValue() > Integer.MIN_VALUE
//                    && field.maxValue() != Integer.MAX_VALUE) {
//                sb.append("minValue :" + field.minLength() + ",\n");
//                sb.append("maxValue :" + field.maxLength() + ",\n");
//
//                sb.append("lessMin : '" + field.displayName() + "不允许小于"
//                        + field.minValue() + "',\n");
//                sb.append("greatMax : '" + field.displayName() + "不允许大于"
//                        + field.maxValue() + "',\n");
//            }
//        }
//
//        if (field.validateType().equals(VALIDATE_TYPE.ALLOW_INPUT_OPTION)) {
//            sb.append("options :" + field.allowInputOptions() + ",\n");
//        }
//        if (!StringUtility.isNullOrEmpty(field.defaultValue())) {
//            sb.append("defaultValue:\"" + field.defaultValue() + "\",\n");
//        }
//
//        String vmsg = CONFIG.validate_error_msg.get("zh_cn").get(
//                field.validateType());
//        if (vmsg != null) {
//            vmsg = vmsg.replace("$diaplayName", field.displayName());
//            vmsg = vmsg.replace("$compareTo", field.compareTo());
//            sb.append(CONFIG.validate_field.get(field.validateType()) + ":'"
//                    + vmsg + "'\n");
//            return sb.toString();
//        } else {
//            return sb.substring(0, sb.length() - ",\n".length());
//        }
//    }
//
//}
