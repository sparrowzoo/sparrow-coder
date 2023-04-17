package com.sparrow.coding;

import com.sparrow.utility.FileUtility;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 1. 递归遍历目录，并复制全部文件
 *  1.1 文件IO的API操作
 *  1.2 递归算法
 * 2. 对应的artifactId 替换为指定的名字
 * 3. 依赖他的所有的pom 也需要替换
 */
public class ScaffoldCopier {
    private static Logger logger = LoggerFactory.getLogger(ScaffoldCopier.class);

    public static void main(String[] args2) {
        String[] args = new String[6];
        //args[0] = "/Users/zhanglizhi/workspace/tedu/tarena-tp-example";
        //sparrow example 脚手架所在目录
        args[0] = "/Users/zhanglizhi/workspace/sparrow/sparrow-zoo/sparrow-example";
        //脚手架新的名字
        args[1] = "newexample";
        try {
            copy(args[0], args[1]);
        } catch (Exception e) {
            logger.error("{} copy errror", args[0], e);
        }
    }

    private static void copy(String source, String targetPackage) {
        File directory = new File(source);
        FileUtility.FolderFilter folderFilter = (sourceFile, targetFile) -> {
            //如果是隐藏文件夹，跳过
            if (sourceFile.startsWith(".")) {
                return true;
            }
            //如果是target文件夹，跳过
            if (sourceFile.contains("target") || sourceFile.equalsIgnoreCase("target")) {
                return true;
            }
            return false;
        };

        String[] directoryList = directory.list();
        if (directoryList == null || directoryList.length == 0) {
            logger.error("directory [{}] is empty", source);
            return;
        }
        for (String childDirectory : directoryList) {
            //过滤掉不需要的文件夹
            if (folderFilter.filter(childDirectory, childDirectory)) {
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
            }, folderFilter);
        }
    }
}
