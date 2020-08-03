package com.sparrow.coding;

import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.orm.JDBCTemplate;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.FileUtility;

/**
 * Created by harry on 2015/5/11.
 */
public class GenerateSql {
    private final static String EVENT="event.sql";

    public static void main(String[] args) {
        ApplicationContext.getContainer().init();
        String rootPath = EnvironmentSupport.getInstance()
                .getApplicationSourcePath()+"/src/main/resources/template/sql/";
        String fileName=rootPath+EVENT;
        String sql= FileUtility.getInstance().readFileContent(fileName);
        for (int i=0;i<20;i++){
            String tempSql= String.format(sql,i);
            try {
                JDBCTemplate.getInstance().executeUpdate(tempSql);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(tempSql);
        }
        System.exit(0);
    }
}
