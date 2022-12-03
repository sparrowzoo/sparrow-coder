package com.sparrow.coding;

import com.sparrow.coding.config.EnvironmentContext;
import com.sparrow.coding.support.enums.ClassKey;
import com.sparrow.orm.*;

import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;

import java.io.IOException;
import java.util.Map;

public class CodeGenerator {

    private EnvironmentContext environmentContext;

    public CodeGenerator() throws IOException {
        this.environmentContext = new EnvironmentContext();
    }

    public void param(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(ClassKey.PARAM);
    }

    public void query(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(ClassKey.QUERY);
    }

    public void assemble(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(ClassKey.ASSEMBLE);
    }

    public void converter(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(ClassKey.DATA_CONVERTER);
    }
    public void vo(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(ClassKey.VO);
    }

    public void bo(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(ClassKey.BO);
    }

    public void dao(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(ClassKey.DAO);
    }

    public void daoImpl(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(ClassKey.DAO_IMPL);
    }


    public void service(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(ClassKey.SERVICE);
    }

    public void repository(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(ClassKey.REPOSITORY);
    }

    public void repositoryImpl(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(ClassKey.REPOSITORY_IMPL);
    }

    public void controller(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
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
                    e.printStackTrace();
                }
            }

            String destTablePath = environmentContext.getSplitTableCreateDDLPath(originTableName, i);
            FileUtility.getInstance().writeFile(destTablePath, tempSql);
            System.err.print(String.format("table create ddl write to %s\n", destTablePath));
        }
    }

    public void generaCreateDDL(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        AbstractEntityManagerAdapter managerAdapter = new SparrowEntityManager(po);
        String tablePath = this.environmentContext.getTableCreateDDLPath(tableConfig.getOriginTableName());
        String sql = managerAdapter.getCreateDDL();
        System.err.println(sql);
        FileUtility.getInstance().writeFile(tablePath, sql);
        System.err.printf(String.format("table create ddl write to %s\n", tablePath));
    }

    public void generateTableTemplate(Class po)
        throws Exception {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        EntityManager entityManager = tableConfig.getEntityManager();
        String originTableName = tableConfig.getOriginTableName();
        Map<String, Field> fieldMap = entityManager.getFieldMap();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("table_name=%s\n", originTableName + "\n#" + originTableName + "驼峰并去掉前缀（首字母大写e.g UserActivity)"));
        sb.append(String.format("display_name=%s\n", po.getSimpleName()));
        int i = 0;
        for (String property : fieldMap.keySet()) {
            sb.append(String.format("%s.display_name=%s\n", fieldMap.get(property).getColumnName(), property));
            sb.append(String.format("%s.display_add=true\n", fieldMap.get(property).getColumnName()));
            sb.append(String.format("%s.display_update=true\n", fieldMap.get(property).getColumnName()));
            sb.append(String.format("%s.display_detail=true\n", fieldMap.get(property).getColumnName()));
            sb.append(String.format("%s.display_list=true\n", fieldMap.get(property).getColumnName()));
            sb.append(String.format("%s.display_image=false\n", fieldMap.get(property).getColumnName()));
            sb.append(String.format("%s.display_hyper_link=false\n", fieldMap.get(property).getColumnName()));
            sb.append(String.format("%s.width=80\n", fieldMap.get(property).getColumnName()));
            sb.append(String.format("%s.height=30\n", fieldMap.get(property).getColumnName()));
            sb.append(String.format("%s.display_order_no=%s\n", fieldMap.get(property).getColumnName(), i++));
            sb.append("\n#############################################################\n");
        }

        String tableConfigPath = this.environmentContext.getTableTemplateConfigPath(tableConfig.getOriginTableName());
        FileUtility.getInstance().writeFile(tableConfigPath, sb.toString());
        System.err.printf(String.format("table template write to %s\n", tableConfigPath));
    }
}
