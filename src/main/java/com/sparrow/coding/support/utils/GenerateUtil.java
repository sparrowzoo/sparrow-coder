//package com.sparrow.coding.support.utils;
//
//import com.sparrow.coding.config.CONFIG;
//import com.sparrow.coding.support.enums.PACKAGE_KEY;
//import com.sparrow.support.EnvironmentSupport;
//import com.sparrow.utility.Config;
//import com.sparrow.utility.FileUtility;
//import com.sparrow.utility.StringUtility;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class GenerateUtil {
//    static Logger logger = LoggerFactory.getLogger(GenerateUtil.class);
//
//    private static void write(String pageName, String tableName, String content) {
//        // 写jsp
//        String module = Config.getValue(CONFIG.API_MODULE);
//        String jspPathKey = module + "_" + CONFIG.JSP_PATH;
//        String jspPath = Config.getValue(jspPathKey).replace(
//                "$RESOURCE_WORKSPACE", Config.getValue(CONFIG.RESOURCE_WORKSPACE))
//                        .replace("$WORKSPACE", Config.getValue(CONFIG.WORKSPACE))
//                + "\\";
//        FileUtility.getInstance().writeFile(
//                jspPath + "\\" + tableName + "\\" + pageName + ".jsp", content);
//    }
//
//
//
//
//    public static void writeNewPage(String tableName, String content) {
//        write("new", tableName, content);
//    }
//
//    public static void writeManagePage(String tableName, String content) {
//        write("manage", tableName, content);
//    }
//}
