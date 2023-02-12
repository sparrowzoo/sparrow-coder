package com.sparrow.coding.config;

import com.sparrow.coding.support.enums.ClassKey;
import com.sparrow.coding.support.enums.PlaceholderKey;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.DateTimeUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class EnvironmentContext {

    /**
     * current author
     */
    private String author;

    /**
     * sparrow coder config
     */
    private Properties config;

    /**
     * config path
     */
    private String configPath;

    private String tableConfig;

    private EnvironmentSupport environmentSupport = EnvironmentSupport.getInstance();

    private FileUtility fileUtility = FileUtility.getInstance();

    public EnvironmentContext() throws IOException {
        this.tableConfig = System.getenv("SPARROW_TABLE_CONFIG");
        if (StringUtility.isNullOrEmpty(tableConfig)) {
            System.err.println("ERROR please set [Environment Variable] 'SPARROW_TABLE_CONFIG'");
            System.exit(0);
        }

        String configPath = System.getenv("SPARROW_CONFIG_PATH");

        InputStream configStream;

        if (StringUtility.isNullOrEmpty(configPath) || "template".equals(configPath)) {
            configStream = this.environmentSupport.getFileInputStreamInCache("/template/config.properties");
            configPath = "/template";
        } else if ("template-mybatis".equals(configPath)) {
            configPath = "/template-mybatis";
            configStream = this.environmentSupport.getFileInputStreamInCache("/template-mybatis/config.properties");
        } else {
            configStream = new FileInputStream(new File(configPath + "/config.properties"));
        }

        this.configPath = configPath;
        Properties config = new Properties();
        config.load(configStream);
        if (configStream == null) {
            System.err.println("template config file can't read");
        }
        this.config = config;
        this.author = config.getProperty(CoderConfig.AUTHOR);
        System.out.printf("author is %s\n", this.author);
    }

    public String getAuthor() {
        return this.author;
    }

    public String getTableTemplateConfigPath(String originTableName) {
        return tableConfig + File.separator + "table_template_config" + File.separator + originTableName + ".properties";
    }

    public String getTableCreateDDLPath(String originTableName) {
        return tableConfig + File.separator + "ddl" + File.separator + originTableName + ".sql";
    }

    public String getSplitTableCreateDDLPath(String originTableName, int i) {
        return tableConfig + File.separator + "ddl" + File.separator + originTableName + File.separator + i + ".sql";
    }

    public String getPackage(ClassKey packageKey) {
        return this.config.getProperty(CoderConfig.PACKAGE_PREFIX + packageKey.name().toLowerCase());
    }

    public String getClassName(ClassKey packageKey, String tableName) {
        String source = config.getProperty(CoderConfig.CLASS_PREFIX + packageKey.name().toLowerCase());
        if (tableName == null) {
            return source;
        }
        return source.replace(PlaceholderKey.$table_name.name(), tableName);
    }

    public String getConfigPath() {
        return this.configPath;
    }

    public String readConfigContent(String configFile) {
        String configFilePath = this.configPath + "/" + configFile;
        System.err.printf("config file path is [%s]\n", configFilePath);
        InputStream inputStream = EnvironmentSupport.getInstance().getFileInputStreamInCache(configFilePath);
        if (inputStream == null) {
            System.err.printf("[%s] can't read\n", configFilePath);
        }
        return FileUtility.getInstance().readFileContent(inputStream, Constant.CHARSET_UTF_8);
    }

    public String readManageStart() {
        return this.readConfigContent("managestart.txt");
    }

    public String readManageEnd() {
        return this.readConfigContent("manageend.txt");
    }

    public String readNewStart() {
        return this.readConfigContent("newstart.txt");
    }

    public String readNewEnd() {
        return this.readConfigContent("newend.txt");
    }

    public class TableConfig {
        private String originTableName;

        private Map<String, String> placeHolder;

        private Properties tableProperties;

        private String poPackage;

        public String getOriginTableName() {
            return originTableName;
        }

        public Map<String, String> getPlaceHolder() {
            return placeHolder;
        }

        public String getPoPackage() {
            return poPackage;
        }

        public EntityManager getEntityManager() {
            return entityManager;
        }

        private EntityManager entityManager;

        public TableConfig(Class po) throws IOException {
            this.poPackage = po.getName().substring(0, po.getName().lastIndexOf(Symbol.DOT));
            this.placeHolder = getPlaceHolder(po);
        }

        public String getProject() {
            return config.getProperty(CoderConfig.PROJECT);
        }

        public String getParentModule() {
            return config.getProperty(CoderConfig.MODULE_PREFIX + CoderConfig.MODULE_PARENT_ADMIN);
        }

        public String getModule(ClassKey key) {
            String admin = this.getParentModule();
            return config.getProperty(CoderConfig.MODULE_PREFIX + admin + "." + key.getModule().toLowerCase());
        }

        public String getFullPackage(ClassKey key) {
            return fileUtility.replacePath(poPackage, ClassKey.PO.name(), getPackage(key), ".");
        }

        private String getDisplayValue(String fieldName, String key, String defaultValue) {
            key = String.format("%s.display_%s", fieldName, key);
            return tableProperties.getProperty(key, defaultValue);
        }

        private Boolean getBoolean(String fieldName, String key, String defaultValue) {
            return Boolean.valueOf(this.getDisplayValue(fieldName, key, defaultValue));
        }

        private Integer getInteger(String fieldName, String key, String defaultValue) {
            return Integer.valueOf(this.getDisplayValue(fieldName, key, defaultValue));
        }

        public String getTableName() {
            return StringUtility.setFirstByteUpperCase(StringUtility.underlineToHump(this.originTableName));
        }

        public String getDisplayName() {
            return tableProperties.getProperty("display_name");
        }

        public boolean getDisplayAdd(String fieldName) {
            return this.getBoolean(fieldName, "add", "true");
        }

        public boolean getDisplayUpdate(String fieldName) {
            return this.getBoolean(fieldName, "update", "true");
        }

        public boolean getDisplayList(String fieldName) {
            return this.getBoolean(fieldName, "list", "true");
        }

        public boolean getDisplayDetail(String fieldName) {
            return this.getBoolean(fieldName, "detail", "true");
        }

        public boolean getDisplayImage(String fieldName) {
            return this.getBoolean(fieldName, "image", "true");
        }

        public boolean getDisplayHyperLink(String fieldName) {
            return this.getBoolean(fieldName, "hyper_link", "true");
        }

        public Integer getDisplayWidth(String fieldName) {
            return this.getInteger(fieldName, "width", "200");
        }

        public Integer getDisplayHeight(String fieldName) {
            return this.getInteger(fieldName, "height", "30");
        }

        public Integer getDisplayOrderNo(String fieldName) {
            return this.getInteger(fieldName, "orderNo", "1");
        }

        private Map<String, String> getPlaceHolder(Class po) throws IOException {
            this.entityManager = new SparrowEntityManager(po);
            String originTableName = entityManager.getTableName();
            this.originTableName = originTableName;

            this.tableProperties = new Properties();
            String tableTemplateConfigPath = getTableTemplateConfigPath(this.originTableName);
            File tablePropertyFile = new File(tableTemplateConfigPath);
            if (tablePropertyFile.exists()) {
                tableProperties.load(new FileInputStream(tablePropertyFile));
            } else {
                System.err.printf("table template config not exist [%s] \n", tableTemplateConfigPath);
            }

            String tableName = this.getTableName();
            String tableDisplayName = this.getDisplayName();
            String primaryPropertyName = entityManager.getPrimary().getName();

            Map<String, String> context = new TreeMap<>((o1, o2) -> o2.compareTo(o1));

            context.put(PlaceholderKey.$origin_table_name.name(), originTableName);
            context.put(PlaceholderKey.$table_name.name(), tableName);
            context.put(PlaceholderKey.$lower_table_name.name(), StringUtility.setFirstByteLowerCase(tableName));
            context.put(PlaceholderKey.$display_name.name(), tableDisplayName);
            context.put(PlaceholderKey.$date.name(), DateTimeUtility
                .getFormatCurrentTime("yyyy-MM-dd HH:mm:ss"));
            context.put(PlaceholderKey.$author.name(), getAuthor());

            context.put(PlaceholderKey.$package_po.name(), poPackage);
            context.put(PlaceholderKey.$package_bo.name(), this.getFullPackage(ClassKey.BO));
            context.put(PlaceholderKey.$package_param.name(), this.getFullPackage(ClassKey.PARAM));
            context.put(PlaceholderKey.$package_query.name(), this.getFullPackage(ClassKey.QUERY));
            context.put(PlaceholderKey.$package_dto.name(), this.getFullPackage(ClassKey.DTO));
            context.put(PlaceholderKey.$package_vo.name(), this.getFullPackage(ClassKey.VO));

            context.put(PlaceholderKey.$package_dao.name(), this.getFullPackage(ClassKey.DAO));
            context.put(PlaceholderKey.$package_repository.name(), this.getFullPackage(ClassKey.REPOSITORY));
            context.put(PlaceholderKey.$package_repository_impl.name(), this.getFullPackage(ClassKey.REPOSITORY_IMPL));
            context.put(PlaceholderKey.$package_data_converter.name(), this.getFullPackage(ClassKey.DATA_CONVERTER));
            context.put(PlaceholderKey.$package_assemble.name(), this.getFullPackage(ClassKey.ASSEMBLE));

            context.put(PlaceholderKey.$package_dao_impl.name(), this.getFullPackage(ClassKey.DAO_IMPL));
            context.put(PlaceholderKey.$package_service.name(), this.getFullPackage(ClassKey.SERVICE));
            context.put(PlaceholderKey.$package_controller.name(), this.getFullPackage(ClassKey.CONTROLLER));

            context.put(PlaceholderKey.$class_po.name(), getClassName(ClassKey.PO, tableName));
            context.put(PlaceholderKey.$class_dao.name(), getClassName(ClassKey.DAO, tableName));
            context.put(PlaceholderKey.$class_impl_dao.name(),
                getClassName(ClassKey.DAO_IMPL, tableName));
            context.put(PlaceholderKey.$class_service.name(),
                getClassName(ClassKey.SERVICE, tableName));

            context.put(PlaceholderKey.$class_repository.name(), getClassName(ClassKey.REPOSITORY, tableName));
            context.put(PlaceholderKey.$class_repositoryImpl.name(), getClassName(ClassKey.REPOSITORY_IMPL, tableName));
            context.put(PlaceholderKey.$class_controller.name(), getClassName(ClassKey.CONTROLLER, tableName));
            context.put(PlaceholderKey.$object_po.name(),
                StringUtility.setFirstByteLowerCase(context.get(PlaceholderKey.$class_po.name())));

            context.put(PlaceholderKey.$object_dao.name(),
                StringUtility.setFirstByteLowerCase(context.get(PlaceholderKey.$class_dao.name())));

            context.put(PlaceholderKey.$object_service.name(),
                StringUtility.setFirstByteLowerCase(context.get(PlaceholderKey.$class_service.name())));

            context.put(PlaceholderKey.$primary_property_name.name(), primaryPropertyName);
            context.put(PlaceholderKey.$upper_primary_property_name.name(), StringUtility.setFirstByteUpperCase(primaryPropertyName));
            context.put(PlaceholderKey.$primary_type.name(), entityManager.getPrimary().getType().getSimpleName());
            context.put(PlaceholderKey.$sql_insert.name(), entityManager.getInsert());
            context.put(PlaceholderKey.$sql_delete.name(), entityManager.getDelete());
            context.put(PlaceholderKey.$sql_update.name(), entityManager.getUpdate());
            context.put(PlaceholderKey.$field_list.name(), entityManager.getFields());
            //context.put(REPLACE_KEY.$sql_query_one.name(), entityManager);
            //context.put(REPLACE_KEY.$result_map.name(), entityManager.toString());
            return context;
        }

        public String getFullPath(String workspace, ClassKey k) {
            String project = this.getProject();
            String parentModule = this.getParentModule();
            String modulePath = this.getModule(k);
            String fullPath = workspace + File.separator
                + project + File.separator
                + parentModule + File.separator
                + modulePath + File.separator
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + this.getFullPackage(k).replace(Symbol.DOT, File.separator);
            System.out.println("write to " + fullPath);
            return fullPath;
        }

        public void write(ClassKey packageKey) {
            String workspace = config.getProperty(CoderConfig.WORKSPACE);
            System.err.printf("current path is [%s]\n", workspace);
            String content = readConfigContent(packageKey.getTemplate());
            content = StringUtility.replace(content.trim(), this.placeHolder);
            String extension = ".java";
            if (content.trim().startsWith("<?xml")) {
                extension = ".xml";
            }

            String fullPath = this.getFullPath(workspace, packageKey);
            String className = getClassName(packageKey, getTableName());
            FileUtility.getInstance().writeFile(fullPath + File.separator + className + extension,
                content);
        }
    }
}