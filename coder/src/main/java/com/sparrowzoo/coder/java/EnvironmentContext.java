package com.sparrowzoo.coder.java;

import com.sparrowzoo.coder.config.CoderConfig;
import com.sparrowzoo.coder.config.EnvConfig;
import com.sparrowzoo.coder.support.utils.ConfigUtils;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.DateTimeUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class EnvironmentContext {
    private static Logger logger = LoggerFactory.getLogger(EnvironmentContext.class);
    /**
     * sparrow coder config
     */
    private Properties config;

    /**
     * current author
     */
    private String author;

    /**
     * 工作区
     */
    private String workspace;
    /**
     * config path
     * <p>
     * 后端模板文件所在的根目录
     */
    private String backendTemplateHome;

    /**
     * 表的DDL 所在根目录
     */
    private String tableOutputGenerateHome;

    /**
     * 项目
     */
    private String project;
    /**
     * 父模块名
     */
    private String parentModule;

    private EnvironmentSupport environmentSupport = EnvironmentSupport.getInstance();

    private FileUtility fileUtility = FileUtility.getInstance();

    public EnvironmentContext(String sparrowConfig) throws IOException {
        this.config = ConfigUtils.initPropertyConfig(sparrowConfig);
        String coderHome = System.getenv(EnvConfig.SPARROW_CODER_HOME);
        if (StringUtility.isNullOrEmpty(coderHome)) {
            logger.error("[{}]环境变未配置，请source /etc/profile 或者 source ~/.bash_profile\n如果依然无法读取,请重启电脑！！！！", EnvConfig.SPARROW_CODER_HOME);
            return;
        }
        this.author = this.config.getProperty(CoderConfig.AUTHOR);
        this.workspace = config.getProperty(CoderConfig.WORKSPACE);
        String userHome = System.getProperty("user.home");
        this.workspace = this.workspace.replace("${user.home}", userHome);
        this.project = config.getProperty(CoderConfig.PROJECT);
        this.parentModule = config.getProperty(CoderConfig.MODULE_PREFIX + CoderConfig.MODULE_PARENT_ADMIN);
        this.backendTemplateHome = config.getProperty(CoderConfig.BACKEND_TEMPLATE_HOME);
        this.backendTemplateHome = this.backendTemplateHome.replace(PlaceholderKey.$coder_home.name(), coderHome);
        this.tableOutputGenerateHome = config.getProperty(CoderConfig.TABLE_OUTPUT_HOME);
        System.out.println("table output home " + this.tableOutputGenerateHome);
        this.tableOutputGenerateHome = this.tableOutputGenerateHome.replace(PlaceholderKey.$coder_home.name(), coderHome);
        System.out.println("code home " + coderHome);
        this.tableOutputGenerateHome = this.tableOutputGenerateHome.replace(PlaceholderKey.$workspace.name(), workspace);
        System.out.println("workspace " + workspace);
        this.tableOutputGenerateHome = this.tableOutputGenerateHome.replace(PlaceholderKey.$project.name(), project);
        System.out.println("project " + project);
        System.out.println("table output home " + this.tableOutputGenerateHome);
        System.out.printf("author is %s\n", this.author);
    }

    public String getTableCreateDDLPath(String originTableName) {
        return tableOutputGenerateHome + File.separator + "ddl" + File.separator + originTableName + ".sql";
    }

    public String getSplitTableCreateDDLPath(String originTableName, int i) {
        return tableOutputGenerateHome + File.separator + "ddl" + File.separator + originTableName + File.separator + i + ".sql";
    }

    public String getPackage(ClassKey classKey) {
        return this.config.getProperty(CoderConfig.PACKAGE_PREFIX + classKey.name().toLowerCase());
    }

    public String getClassName(ClassKey classKey, String persistenceClassName) {
        String source = config.getProperty(CoderConfig.CLASS_PREFIX + classKey.name().toLowerCase());
        if (persistenceClassName == null) {
            return source;
        }
        return source.replace(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
    }

    public Properties getMybatisConfig() throws IOException {
        String mybatisConfig = this.backendTemplateHome + "/" + ClassKey.DAO_MYBATIS.getTemplate();
        System.err.printf("config file path is [%s]\n", mybatisConfig);
        InputStream inputStream = EnvironmentSupport.getInstance().getFileInputStreamInCache(mybatisConfig);
        if (inputStream == null) {
            System.err.printf("[%s] can't read\n", mybatisConfig);
        }
        Properties mybatisProperties = new Properties();
        mybatisProperties.load(inputStream);
        return mybatisProperties;
    }

    public String readConfigContent(String templateFileName) {
        String configFilePath = this.backendTemplateHome + "/" + templateFileName;
        System.err.printf("config file path is [%s]\n", configFilePath);
        InputStream inputStream = EnvironmentSupport.getInstance().getFileInputStreamInCache(configFilePath);
        if (inputStream == null) {
            System.err.printf("[%s] can't read\n", configFilePath);
        }
        return FileUtility.getInstance().readFileContent(inputStream, StandardCharsets.UTF_8.name());
    }

    public class Config {
        /**
         * 原始表名 通过entity JPA @Table 获取
         */
        private String originTableName;

        /**
         * 持久化类名
         */
        private String persistenceClassName;

        /**
         * 占位符
         */
        public Map<String, String> placeHolder;

        /**
         * 包名都根据po对象生成
         */
        private String poPackage;

        public String getOriginTableName() {
            return originTableName;
        }

        private EntityManager entityManager;

        public Config(Class<?> po) {
            this.poPackage = po.getName().substring(0, po.getName().lastIndexOf(Symbol.DOT));
            this.entityManager = new SparrowEntityManager(po);
            this.placeHolder = initPlaceHolder();
        }

        public String getModule(ClassKey key) {
            String moduleKey = CoderConfig.MODULE_PREFIX;
            if (!StringUtility.isNullOrEmpty(parentModule)) {
                moduleKey += parentModule + "." + key.getModule().toLowerCase();
            } else {
                moduleKey += key.getModule().toLowerCase();
            }
            String modulePath = config.getProperty(moduleKey);
            if (modulePath == null) {
                logger.error("module path is null, module key is [{}]", moduleKey);
                System.exit(0);
            }
            return StringUtility.replace(modulePath, this.placeHolder);
        }

        public String getFullPackage(ClassKey key) {
            String packageOfClazz = getPackage(key);
            return fileUtility.replacePath(poPackage, ClassKey.PO.name(), packageOfClazz, Symbol.DOT);
        }

        private Map<String, String> initPlaceHolder() {
            this.originTableName = entityManager.getTableName();
            this.persistenceClassName =entityManager.getSimpleClassName();
            String primaryPropertyName = entityManager.getPrimary().getPropertyName();
            Map<String, String> context = new TreeMap<>(Comparator.reverseOrder());
            String modulePrefix = config.getProperty(CoderConfig.MODULE_PREFIX + "prefix");
            context.put(PlaceholderKey.$module_prefix.name(), modulePrefix);
            context.put(PlaceholderKey.$origin_table_name.name(), this.originTableName);
            context.put(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
            context.put(PlaceholderKey.$persistence_object_name.name(), StringUtility.setFirstByteLowerCase(persistenceClassName));
            context.put(PlaceholderKey.$persistence_object_by_horizontal.name(), StringUtility.humpToLower(persistenceClassName, '-'));
            context.put(PlaceholderKey.$persistence_object_by_slash.name(), StringUtility.humpToLower(persistenceClassName, '/'));
            context.put(PlaceholderKey.$persistence_object_by_dot.name(), StringUtility.humpToLower(persistenceClassName, '.'));

            context.put(PlaceholderKey.$date.name(), DateTimeUtility
                    .getFormatCurrentTime("yyyy-MM-dd HH:mm:ss"));
            context.put(PlaceholderKey.$author.name(), author);

            context.put(PlaceholderKey.$package_po.name(), poPackage);
            context.put(PlaceholderKey.$package_bo.name(), this.getFullPackage(ClassKey.BO));
            context.put(PlaceholderKey.$package_param.name(), this.getFullPackage(ClassKey.PARAM));

            context.put(PlaceholderKey.$package_batch_param.name(), this.getFullPackage(ClassKey.BATCH_OPERATE_PARAM));

            context.put(PlaceholderKey.$package_query.name(), this.getFullPackage(ClassKey.QUERY));
            context.put(PlaceholderKey.$package_dto.name(), this.getFullPackage(ClassKey.DTO));
            context.put(PlaceholderKey.$package_vo.name(), this.getFullPackage(ClassKey.VO));

            context.put(PlaceholderKey.$package_dao.name(), this.getFullPackage(ClassKey.DAO));
            String pagerQuery = this.getFullPackage(ClassKey.PAGER_QUERY);
            context.put(PlaceholderKey.$package_pager_query.name(), StringUtility.replace(pagerQuery, context));
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

        public String getFullPath(ClassKey k) {
            String modulePath = this.getModule(k);
            String path = "";
            if (ClassKey.DAO_MYBATIS.getModule().equals(k.getModule())) {
                path = "src" + File.separator
                        + "main" + File.separator
                        + "resources" + File.separator + "mapper";
            } else {
                String fullPackage = this.getFullPackage(k);
                path = "src" + File.separator
                        + "main" + File.separator
                        + "java" + File.separator
                        + fullPackage.replace('.', File.separatorChar);
            }
            String projectPath = StringUtility.isNullOrEmpty(project) ? "" : project + File.separator;
            String parentModulePath = StringUtility.isNullOrEmpty(parentModule) ? "" : modulePath + File.separator;
            String module = StringUtility.isNullOrEmpty(modulePath) ? "" : modulePath + File.separator;
            String fullPath = workspace + File.separator
                    + projectPath
                    + parentModulePath
                    + module + path;
            fullPath = StringUtility.replace(fullPath, this.placeHolder);
            System.out.println("write to " + fullPath);
            return fullPath;
        }

        public void writeMybatis(Class<?> po, EnvironmentContext environmentContext) throws IOException {
            MybatisEntityManager entityManager = new MybatisEntityManager(po, environmentContext);
            entityManager.init();
            String content = entityManager.getXml();
            content = StringUtility.replace(content.trim(), this.placeHolder);
            System.out.println(content);
            String extension = ".xml";
            String fullPath = this.getFullPath(ClassKey.DAO_MYBATIS);
            String className = getClassName(ClassKey.DAO_MYBATIS, persistenceClassName);
            FileUtility.getInstance().writeFile(fullPath + File.separator + className + extension,
                    content);
        }

        public void write(ClassKey classKey) throws IOException {
            System.err.printf("current path is [%s]\n", workspace);

            String licensed = FileUtility.getInstance().readFileContent("/Licensed.txt");
            String content = readConfigContent(classKey.getTemplate());
            content = StringUtility.replace(content.trim(), this.placeHolder);
            System.out.println(content);
            String extension = ".java";
            String fullPath = this.getFullPath(classKey);
            String className = getClassName(classKey, persistenceClassName);
            content = licensed + "\n" + content;
            FileUtility.getInstance().writeFile(fullPath + File.separator + className + extension,
                    content);
        }
    }
}
