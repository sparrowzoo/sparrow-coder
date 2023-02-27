package com.sparrow.coding.java;

import com.sparrow.coding.java.enums.ClassKey;
import com.sparrow.orm.AbstractEntityManagerAdapter;
import com.sparrow.orm.JDBCTemplate;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeGenerator {
    private static Logger logger = LoggerFactory.getLogger(CodeGenerator.class);
    private EnvironmentContext environmentContext;

    public CodeGenerator() throws IOException {
        this.environmentContext = new EnvironmentContext();
    }

    public void param(Class<?> po) throws IOException {
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.PARAM);
    }

    public void query(Class<?> po) throws IOException {
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.QUERY);
    }

    public void assemble(Class<?> po) throws IOException {
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.ASSEMBLE);
    }

    public void converter(Class<?> po){
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.DATA_CONVERTER);
    }

    public void vo(Class<?> po){
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.VO);
    }

    public void bo(Class<?> po) throws IOException {
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.BO);
    }

    public void dao(Class<?> po) throws IOException {
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.DAO);
    }

    public void daoImpl(Class<?> po) {
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.DAO_IMPL);
    }

    public void service(Class<?> po) {
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.SERVICE);
    }

    public void repository(Class<?> po){
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.REPOSITORY);
    }

    public void repositoryImpl(Class<?> po) {
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.REPOSITORY_IMPL);
    }

    public void controller(Class<?> po){
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        tableConfig.write(ClassKey.CONTROLLER);
    }

    public void generaCreateNDDL(String originTableName, Integer n, boolean create) {
        String originTableFullPath = environmentContext.getTableCreateDDLPath(originTableName);
        String originTableContent = FileUtility.getInstance().readFileContent(originTableFullPath);
        for (int i = 0; i < n; i++) {
            String tempSql = originTableContent.replace(Constant.TABLE_SUFFIX, "_" + i);
            if (create) {
                try {
                    String[] ddlArray = tempSql.split(";");
                    for (String ddl : ddlArray) {
                        if (!StringUtility.isNullOrEmpty(ddl)) {
                            JDBCTemplate.getInstance().executeUpdate(ddl);
                        }
                    }
                } catch (Exception e) {
                    logger.error("create ddl sql error", e);
                }
            }

            String destTablePath = environmentContext.getSplitTableCreateDDLPath(originTableName, i);
            FileUtility.getInstance().writeFile(destTablePath, tempSql);
            System.err.printf("table create ddl write to %s\n", destTablePath);
        }
    }

    public void generaCreateDDL(Class<?> po) throws IOException {
        EnvironmentContext.Config tableConfig = environmentContext.new Config(po);
        AbstractEntityManagerAdapter managerAdapter = new SparrowEntityManager(po);
        String tablePath = this.environmentContext.getTableCreateDDLPath(tableConfig.getOriginTableName());
        String sql = managerAdapter.getCreateDDL();
        System.err.println(sql);
        FileUtility.getInstance().writeFile(tablePath, sql);
        System.err.printf("table create ddl write to %s\n", tablePath);
    }
}
