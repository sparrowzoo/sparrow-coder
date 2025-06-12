package com.sparrow.coder.java;

import com.sparrow.coding.api.TableConfigRegistry;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.utility.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.io.File;
import java.io.IOException;

/**
 * 1. 递归遍历目录，并复制全部文件
 * 1.1 文件IO的API操作
 * 1.2 递归算法
 * 2. 对应的artifactId 替换为指定的名字
 * 3. 依赖他的所有的pom 也需要替换
 */
@Named
public class ScaffoldCopier {
    private static Logger logger = LoggerFactory.getLogger(ScaffoldCopier.class);

    static void copy(TableConfigRegistry registry) {
        String scaffoldHome = new FileNameBuilder(registry.getWorkspace()).joint(registry.getScaffold()).build();
        ProjectConfig projectConfig = registry.getProjectConfig();
        File directory = new File(scaffoldHome);
        FileUtility.FolderFilter folderFilter = (sourceFile) -> {
            if (!projectConfig.getWrapWithParent()) {
                if (sourceFile.contains("admin/pom.xml")) {
                    return true;
                }
            }
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
            logger.error("directory [{}] is empty", scaffoldHome);
            return;
        }
        for (String childDirectory : directoryList) {
            //过滤掉不需要的文件夹
            if (folderFilter.filter(childDirectory)) {
                continue;
            }

            String sourceDirectoryPath = new FileNameBuilder(scaffoldHome).joint(childDirectory).build();
            FileUtility.getInstance().recurseCopy(sourceDirectoryPath, (sourceFileName) -> {
                String targetPath = sourceFileName.replace(scaffoldHome, "").replace("example", projectConfig.getModulePrefix());
                if (!projectConfig.getWrapWithParent()) {
                    targetPath = targetPath.replace("admin/", "");
                }
                targetPath = new FileNameBuilder(registry.getWorkspace()).joint(String.valueOf(projectConfig.getCreateUserId()))
                        .joint(registry.getProjectConfig().getName()).joint(targetPath).build();
                String content = FileUtility.getInstance().readFileContent(sourceFileName);
                content = content.replaceAll("example", projectConfig.getModulePrefix());
                try {
                    FileUtility.getInstance().writeFile(targetPath, content);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, folderFilter);
        }
    }
}
