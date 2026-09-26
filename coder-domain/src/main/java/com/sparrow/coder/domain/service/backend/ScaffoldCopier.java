/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.coder.domain.service.backend;

import com.sparrow.coder.domain.bo.ProjectConfigBO;
import com.sparrow.coder.domain.service.EnvConfig;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.io.FileCopier;
import com.sparrow.io.FolderFilter;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.utility.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 1. 递归遍历目录，并复制全部文件
 * 1.1 文件IO的API操作
 * 1.2 递归算法
 * 2. 对应的artifactId 替换为指定的名字
 * 3. 依赖他的所有的pom 也需要替换
 */
public class ScaffoldCopier {
    private static Logger logger = LoggerFactory.getLogger(ScaffoldCopier.class);

    private static void copyFrontend(TableConfigRegistry registry) {
        EnvConfig config = registry.getProject().getEnvConfig();
        ProjectConfigBO projectConfig = registry.getProject().getProjectConfig();
        String scaffoldHome = new FileNameBuilder(config.getWorkspace()).joint("sparrow-example").build();

        String frontScaffoldHome = new FileNameBuilder(config.getWorkspace()).joint(config.getFrontProjectRoot()).joint("react-next-admin").build();


        File directory = new File(frontScaffoldHome);
        FolderFilter folderFilter = (sourceFile) -> {
            String fileName = sourceFile.replace(scaffoldHome, "");
            if (fileName.equalsIgnoreCase(".gitignore") || fileName.equalsIgnoreCase(".env.development") || fileName.equalsIgnoreCase(".env.production")) {
                return false;
            }
            if (sourceFile.equalsIgnoreCase("node_modules") || sourceFile.equalsIgnoreCase("out")) {
                return true;
            }

            //如果是隐藏文件夹，跳过
            if (sourceFile.startsWith(".")) {
                return true;
            }
            return false;
        };
        FileCopier folderCopier = sourceFileName -> {
            //sourceFileName: /{home}/sparrow/sparrow-js/react-next-admin/package.js
            //scaffoldHome: /{home}/sparrow/sparrow-js/react-next-admin
            String targetFileName = sourceFileName.replace(frontScaffoldHome, "");
            EnvConfig envConfig = registry.getProject().getEnvConfig();
            String home = envConfig.getHome(projectConfig.getCreateUserId());
            String targetPath = new FileNameBuilder(envConfig.getWorkspace()).joint(envConfig.getProjectRoot()).joint(home).joint(registry.getProject().getProjectConfig().getName()).joint("front").joint(targetFileName).build();
            try {
                FileUtility.getInstance().copy(sourceFileName, targetPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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
            String sourceDirectoryPath = new FileNameBuilder(frontScaffoldHome).joint(childDirectory).build();
            FileUtility.getInstance().recurseCopy(sourceDirectoryPath, folderCopier, folderFilter);
        }
    }

    private static void copyBackend(TableConfigRegistry registry) {
        ProjectConfigBO projectConfig = registry.getProject().getProjectConfig();
        String scaffoldHome = new FileNameBuilder(registry.getProject().getEnvConfig().getWorkspace()).joint("sparrow-example").build();
        File directory = new File(scaffoldHome);
        FolderFilter backendFolderFilter = (sourceFile) -> {
            String fileName = sourceFile.replace(scaffoldHome, "");
            if (fileName.equalsIgnoreCase(".gitignore")) {
                return false;
            }
            if (!projectConfig.getWrapWithParent()) {
                if (fileName.equalsIgnoreCase("pom.xml")) {
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
        FileCopier backendFolderCopier = sourceFileName -> {
            //sourceFileName: /{home}/sparrow-example/bom/pom.xml
            //scaffoldHome: /{home}/sparrow-example
            String targetFileName = sourceFileName.replace(scaffoldHome, "").replace("example", projectConfig.getModulePrefix());
            if (!projectConfig.getWrapWithParent()) {
                targetFileName = targetFileName.replace("admin" + File.separator, "");
                targetFileName = targetFileName.replace("admin-", "");
            }
            EnvConfig envConfig = registry.getProject().getEnvConfig();
            String home = envConfig.getHome(projectConfig.getCreateUserId());
            String targetPath = new FileNameBuilder(envConfig.getWorkspace()).joint(envConfig.getProjectRoot()).joint(home).joint(registry.getProject().getProjectConfig().getName()).joint(targetFileName).build();
            String content = null;
            //如果不需要parent 包裹，并且是pom.xml，则需要读admin/pom.xml的内容处理
            if (!projectConfig.getWrapWithParent() && targetFileName.equals(File.separator + "pom.xml")) {
                content = FileUtility.getInstance().readFileContent(sourceFileName);
                content = content.replaceAll("<!--po-->", "<module>" + projectConfig.getModulePrefix() + "-po</module>");
            } else {
                content = FileUtility.getInstance().readFileContent(sourceFileName);
            }

            content = content.replaceAll("example", projectConfig.getModulePrefix());
            if (!projectConfig.getWrapWithParent()) {
                content = content.replaceAll(".admin", "");
            }
            try {
                FileUtility.getInstance().writeFile(targetPath, content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        String[] directoryList = directory.list();
        if (directoryList == null || directoryList.length == 0) {
            logger.error("directory [{}] is empty", scaffoldHome);
            return;
        }
        for (String childDirectory : directoryList) {
            //过滤掉不需要的文件夹
            if (backendFolderFilter.filter(childDirectory)) {
                continue;
            }
            String sourceDirectoryPath = new FileNameBuilder(scaffoldHome).joint(childDirectory).build();
            FileUtility.getInstance().recurseCopy(sourceDirectoryPath, backendFolderCopier, backendFolderFilter);
        }
    }

    public static void copy(TableConfigRegistry registry) {
        copyBackend(registry);
        copyFrontend(registry);
    }
}
