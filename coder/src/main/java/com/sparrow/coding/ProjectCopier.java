package com.sparrow.coding;

import com.sparrow.utility.FileUtility;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectCopier {
    private static Logger logger = LoggerFactory.getLogger(ProjectCopier.class);

    public static void main(String[] args2) {
        String[] args = new String[6];
        args[0] = "/Users/zhanglizhi/workspace/tedu/cs-mall-shopping-cart";
        args[1] = "/Users/zhanglizhi/workspace/tedu/cs-mall-order";
        args[2] = "/Users/zhanglizhi/workspace/tedu/cs-mall-pay-center";
        args[3] = "/Users/zhanglizhi/workspace/tedu/cs-mall-user";
        args[4] = "/Users/zhanglizhi/workspace/tedu/inn-component";
        args[5] = "/Users/zhanglizhi/workspace/tedu/cs-mall-shopping-cart-wrap";
        String target = args[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            String source = args[i];
            try {
                copy(source, target);
            } catch (Exception e) {
                System.out.println(source + " is error");
            }
        }
    }

    private static void copy(String source, String target) {
        File directory = new File(source);
        String[] directoryList = directory.list();
        for (String childDirectory : directoryList) {
            String sourceDirectoryPath = source + File.separator + childDirectory + "/src/main/java";
            File sourceDirectory = new File(sourceDirectoryPath);
            if (sourceDirectory.exists()) {
                FileUtility.getInstance().recurseCopy(sourceDirectoryPath, target);
            }
        }
    }
}
