package com.sparrowzoo.coder.domain.service.backend;

import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 1. 递归遍历目录，并复制全部文件
 * 1.1 文件IO的API操作
 * 1.2 递归算法
 * 2. 对应的artifactId 替换为指定的名字
 * 3. 依赖他的所有的pom 也需要替换
 */
public class ScaffoldCopier {
    private static Logger logger = LoggerFactory.getLogger(ScaffoldCopier.class);

    public static void copy(TableConfigRegistry registry) {
        ProjectConfigBO projectConfig = registry.getProject();
        String scaffoldHome = new FileNameBuilder(registry.getEnvConfig().getWorkspace())
                .joint("sparrow-example").build();
        File directory = new File(scaffoldHome);
        FileUtility.FolderFilter folderFilter = (sourceFile) -> {
            if (!projectConfig.getWrapWithParent()) {
                if (sourceFile.contains("admin" + File.separator + "pom.xml")) {
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
                //sourceFileName: /{home}/workspace/sparrow-example/bom/pom.xml
                //scaffoldHome: /{home}/workspace/sparrow-example
                String targetFileName = sourceFileName.replace(scaffoldHome, "").replace("example", projectConfig.getModulePrefix());
                if (!projectConfig.getWrapWithParent()) {
                    targetFileName = targetFileName.replace("admin" + File.separator, "");
                    targetFileName = targetFileName.replace("admin-", "");
                }
                String home =registry.getEnvConfig().getHome(projectConfig.getCreateUserId());
                String targetPath = new FileNameBuilder(registry.getEnvConfig().getWorkspace())
                        .joint(registry.getEnvConfig().getProjectRoot())
                        .joint(home)
                        .joint(registry.getProject().getName())
                        .joint(targetFileName).build();
                String content = null;
                //如果不需要parent 包裹，并且是pom.xml，则需要读admin/pom.xml的内容处理
                if (!projectConfig.getWrapWithParent() && targetFileName.equals(File.separator + "pom.xml")) {
                    sourceFileName = new FileNameBuilder(scaffoldHome)
                            .joint(targetFileName.replace(File.separator + "pom.xml", ""))
                            .joint("admin")
                            .fileName("pom")
                            .extension(".xml")
                            .build();
                    content = FileUtility.getInstance().readFileContent(sourceFileName);
                    content = content.replaceAll("<!--po-->", "<module>" + projectConfig.getModulePrefix() + "-po</module>");
                } else {
                    content = FileUtility.getInstance().readFileContent(sourceFileName);
                }

                Map<String, String> placeHolder = registry.getFirstTableContext().getPlaceHolder();
                content = StringUtility.replace(content.trim(), placeHolder);
                content = content.replaceAll("example", projectConfig.getModulePrefix());
                if (!projectConfig.getWrapWithParent()) {
                    content = content.replaceAll(".admin", "");
                }
                try {
                    FileUtility.getInstance().writeFile(targetPath, content);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, folderFilter);
        }
    }
}
