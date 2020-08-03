package com.sparrow.coding.config;

import com.sparrow.coding.support.enums.PACKAGE_KEY;
import com.sparrow.coding.support.enums.REPLACE_KEY;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.constant.CONSTANT;
import com.sparrow.protocol.constant.magic.SYMBOL;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.DateTimeUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
            configStream = new FileInputStream(new File(configPath + File.separator + "config.properties"));
        }

        this.configPath = configPath;
        Properties config = new Properties();
        config.load(configStream);
        this.config = config;
        this.author = config.getProperty(CONFIG.AUTHOR);
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

    public String getPackage(PACKAGE_KEY package_key) {
        return this.config.getProperty(CONFIG.PACKAGE_PREFIX + package_key.name().toLowerCase());
    }

    public String getClassName(PACKAGE_KEY package_key, String tableName) {
        String source = config.getProperty(CONFIG.CLASS_PREFIX + package_key.name().toLowerCase());
        if (tableName == null) {
            return source;
        }
        return source.replace(REPLACE_KEY.$table_name.name(), tableName);
    }

    public String getConfigPath() {
        return this.configPath;
    }

    public String readConfigContent(String configFile) {
        return FileUtility.getInstance().readFileContent(
                EnvironmentSupport.getInstance().getFileInputStreamInCache(this.configPath + File.separator + configFile), CONSTANT.CHARSET_UTF_8);
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
            this.poPackage = po.getName().substring(0, po.getName().lastIndexOf(SYMBOL.DOT));
            this.placeHolder = getPlaceHolder(po);
        }

        public String getFullPackage(PACKAGE_KEY key) {
            return fileUtility.replacePath(poPackage, PACKAGE_KEY.PO.name(), getPackage(key), ".");
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
            return tableProperties.getProperty("table_name");
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
            File tablePropertyFile = new File(getTableTemplateConfigPath(this.originTableName));
            if (tablePropertyFile.exists()) {
                tableProperties.load(new FileInputStream(tablePropertyFile));
            }

            String tableName = this.getTableName();
            String tableDisplayName = this.getDisplayName();
            String primaryPropertyName = entityManager.getPrimary().getName();

            Map<String, String> context = new HashMap<String, String>();

            context.put(REPLACE_KEY.$origin_table_name.name(), originTableName);
            context.put(REPLACE_KEY.$table_name.name(), tableName);
            context.put(REPLACE_KEY.$lower_table_name.name(), StringUtility.setFirstByteLowerCase(tableName));
            context.put(REPLACE_KEY.$display_name.name(), tableDisplayName);
            context.put(REPLACE_KEY.$date.name(), DateTimeUtility
                    .getFormatCurrentTime("yyyy-MM-dd HH:mm:ss"));
            context.put(REPLACE_KEY.$author.name(), getAuthor());

            context.put(REPLACE_KEY.$package_po.name(), poPackage);

            context.put(REPLACE_KEY.$package_dao.name(), this.getFullPackage(PACKAGE_KEY.DAO));
            context.put(REPLACE_KEY.$package_impl_dao.name(), this.getFullPackage(PACKAGE_KEY.DAO_IMPL));
            context.put(REPLACE_KEY.$package_service.name(), this.getFullPackage(PACKAGE_KEY.SERVICE));
            context.put(REPLACE_KEY.$package_impl_service.name(), this.getFullPackage(PACKAGE_KEY.SERVICE_IMPL));
            context.put(REPLACE_KEY.$package_controller.name(), this.getFullPackage(PACKAGE_KEY.CONTROLLER));

            context.put(REPLACE_KEY.$class_po.name(), getClassName(PACKAGE_KEY.PO, tableName));
            context.put(REPLACE_KEY.$class_dao.name(), getClassName(PACKAGE_KEY.DAO, tableName));
            context.put(REPLACE_KEY.$class_impl_dao.name(),
                    getClassName(PACKAGE_KEY.DAO_IMPL, tableName));
            context.put(REPLACE_KEY.$class_service.name(),
                    getClassName(PACKAGE_KEY.SERVICE, tableName));
            context.put(REPLACE_KEY.$class_impl_service.name(), getClassName(PACKAGE_KEY.SERVICE_IMPL, tableName));
            context.put(REPLACE_KEY.$class_controller.name(), getClassName(PACKAGE_KEY.CONTROLLER, tableName));

            context.put(REPLACE_KEY.$object_po.name(),
                    StringUtility.setFirstByteLowerCase(context.get(REPLACE_KEY.$class_po.name())));

            context.put(REPLACE_KEY.$object_dao.name(),
                    StringUtility.setFirstByteLowerCase(context.get(REPLACE_KEY.$class_dao.name())));

            context.put(REPLACE_KEY.$object_service.name(),
                    StringUtility.setFirstByteLowerCase(context.get(REPLACE_KEY.$class_service.name())));

            context.put(REPLACE_KEY.$primary_property_name.name(), primaryPropertyName);
            context.put(REPLACE_KEY.$upper_primary_property_name.name(), StringUtility.setFirstByteUpperCase(primaryPropertyName));
            context.put(REPLACE_KEY.$primary_type.name(), entityManager.getPrimary().getType().getSimpleName());
            context.put(REPLACE_KEY.$sql_insert.name(), entityManager.getInsert());
            context.put(REPLACE_KEY.$sql_delete.name(), entityManager.getDelete());
            context.put(REPLACE_KEY.$sql_update.name(), entityManager.getUpdate());
            context.put(REPLACE_KEY.$field_list.name(), entityManager.getFields());
            //context.put(REPLACE_KEY.$sql_query_one.name(), entityManager.ge);
            //context.put(REPLACE_KEY.$result_map.name(), entityManager.toString());
            return context;
        }

        public void write(PACKAGE_KEY k) {
            String currentPath = EnvironmentSupport.getInstance().getApplicationSourcePath();
            String content = readConfigContent(k.getTemplate());
            content = StringUtility.replace(content.trim(), this.placeHolder);
            String extension = ".java";
            if (content.trim().startsWith("<?xml")) {
                extension = ".xml";
            }


            String fullPath = currentPath + File.separator + "src" +
                    File.separator + "main" +
                    File.separator + "java" +
                    File.separator
                    + this.getFullPackage(k).replace(SYMBOL.DOT, File.separator);

            String className = getClassName(k, getTableName());
            FileUtility.getInstance().writeFile(fullPath + File.separator + className + extension,
                    content);
        }
    }
}