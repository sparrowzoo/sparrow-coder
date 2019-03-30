package com.sparrow.coding;

import com.sparrow.coding.config.EnvironmentContext;
import com.sparrow.coding.support.enums.PACKAGE_KEY;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;

import com.sparrow.utility.FileUtility;
import java.io.IOException;
import java.util.Map;

/**
 * Created by harry on 16/8/22.
 */
public class CodeGenerator {

    private EnvironmentContext environmentContext;

    public CodeGenerator() throws IOException {
        this.environmentContext = new EnvironmentContext();
    }

    public void dao(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(PACKAGE_KEY.DAO);
    }

    public void daoImpl(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(PACKAGE_KEY.DAO_IMPL);
    }

    public void service(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(PACKAGE_KEY.SERVICE);
    }

    public void serviceImpl(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(PACKAGE_KEY.SERVICE_IMPL);
    }

    public void controller(Class po) throws IOException {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        tableConfig.write(PACKAGE_KEY.CONTROLLER);
    }

    public void generateTableTemplate(Class po)
        throws Exception {
        EnvironmentContext.TableConfig tableConfig = environmentContext.new TableConfig(po);
        EntityManager entityManager = tableConfig.getEntityManager();
        String originTableName = tableConfig.getOriginTableName();
        Map<String, Field> fieldMap = entityManager.getFieldMap();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("table_name=%s\n",  originTableName+"\n#"+originTableName+"驼峰并去掉前缀（首字母大写e.g UserActivity)"));
        sb.append(String.format("display_name=%s\n",po.getSimpleName()));
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

        String tableConfigPath = this.environmentContext.getTableConfigPath(tableConfig.getOriginTableName());
        FileUtility.getInstance().writeFile(tableConfigPath, sb.toString());
        System.out.printf(String.format("table template write to %s", tableConfigPath));
    }
}
