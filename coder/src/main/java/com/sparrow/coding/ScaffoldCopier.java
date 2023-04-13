package com.sparrow.coding;

import com.sparrow.utility.FileUtility;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScaffoldCopier {
    private static Logger logger = LoggerFactory.getLogger(ScaffoldCopier.class);

    public static void main(String[] args2) {
        String[] args = new String[6];
        //args[0] = "/Users/zhanglizhi/workspace/tedu/tarena-tp-example";
        //args[0] = "/Users/zhanglizhi/workspace/sparrow/sparrow-zoo/sparrow-example";

        //args[1] = "chedan";

        try {
            copy(args[0], args[1]);
        } catch (Exception e) {
            logger.error("{} copy errror", args[0], e);
        }
    }

    private static void copy(String source, String targetPackage) {
        File directory = new File(source);
        String[] directoryList = directory.list();
        for (String childDirectory : directoryList) {
            if (childDirectory.startsWith(".")) {
                continue;
            }
            String sourceDirectoryPath = source + File.separator + childDirectory;
            FileUtility.getInstance().recurseCopy(sourceDirectoryPath, sourceDirectoryPath, (sourceFileName, targetFileName) -> {
                String targetPath = targetFileName.replace("example", targetPackage);
                String content = FileUtility.getInstance().readFileContent(sourceFileName);
                content = content.replaceAll("example", targetPackage);
                try {
                    FileUtility.getInstance().writeFile(targetPath, content);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
