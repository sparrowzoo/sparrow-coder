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


    public String getTableCreateDDLPath(String originTableName) {
        return tableConfig + File.separator + "ddl" + File.separator + originTableName + ".sql";
    }

    public String getSplitTableCreateDDLPath(String originTableName, int i) {
        return tableConfig + File.separator + "ddl" + File.separator + originTableName + File.separator + i + ".sql";
    }

    public String getPackage(ClassKey packageKey) {
        return this.config.getProperty(CoderConfig.PACKAGE_PREFIX + packageKey.name().toLowerCase());
    }

    public String getClassName(ClassKey packageKey, String persistenceClassName) {
        String source = config.getProperty(CoderConfig.CLASS_PREFIX + packageKey.name().toLowerCase());
        if (persistenceClassName == null) {
            return source;
        }
        return source.replace(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
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

        /**
         * 包名都根据po对象生成
         */
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

        public String getPersistenceClassName() {
            return StringUtility.setFirstByteUpperCase(StringUtility.underlineToHump(this.originTableName));
        }

        private Map<String, String> getPlaceHolder(Class po) throws IOException {
            this.entityManager = new SparrowEntityManager(po);
            String originTableName = entityManager.getTableName();
            this.originTableName = originTableName;


            String persistenceClassName = this.getPersistenceClassName();
            String primaryPropertyName = entityManager.getPrimary().getName();

            Map<String, String> context = new TreeMap<>(Comparator.reverseOrder());

            context.put(PlaceholderKey.$origin_table_name.name(), originTableName);
            context.put(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
            context.put(PlaceholderKey.$persistence_object_name.name(), StringUtility.setFirstByteLowerCase(persistenceClassName));
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

            context.put(PlaceholderKey.$class_po.name(), getClassName(ClassKey.PO, persistenceClassName));
            context.put(PlaceholderKey.$class_dao.name(), getClassName(ClassKey.DAO, persistenceClassName));
            context.put(PlaceholderKey.$class_impl_dao.name(),
                getClassName(ClassKey.DAO_IMPL, persistenceClassName));
            context.put(PlaceholderKey.$class_service.name(),
                getClassName(ClassKey.SERVICE, persistenceClassName));

            context.put(PlaceholderKey.$class_repository.name(), getClassName(ClassKey.REPOSITORY, persistenceClassName));
            context.put(PlaceholderKey.$class_repositoryImpl.name(), getClassName(ClassKey.REPOSITORY_IMPL, persistenceClassName));
            context.put(PlaceholderKey.$class_controller.name(), getClassName(ClassKey.CONTROLLER, persistenceClassName));
            context.put(PlaceholderKey.$persistence_object_name.name(),
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
            String className = getClassName(packageKey, getPersistenceClassName());
            FileUtility.getInstance().writeFile(fullPath + File.separator + className + extension,
                content);
        }
    }
}