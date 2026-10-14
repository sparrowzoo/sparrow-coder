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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassPathUtils;

import java.io.File;
import java.io.IOException;

/**
 * 1. 递归遍历目录，并复制全部文件
 * 1.1 文件IO的API操作
 * 1.2 递归算法
 * 2. 对应的artifactId 替换为指定的名字
 * 3. 依赖他的所有的pom 也需要替换
 */
@Slf4j
public class ScaffoldCopier {

    private static void copyFrontend(TableConfigRegistry registry) {
        EnvConfig config = registry.getProject().getEnvConfig();
        ProjectConfigBO projectConfig = registry.getProject().getProjectConfig();
        String backendScaffoldHome = new FileNameBuilder(config.getBackendScaffoldTemplateHome()).joint("sparrow-example").build();
        String frontScaffoldHome = new FileNameBuilder(config.getFrontendScaffoldTemplateHome()).joint("sparrow-js").joint("sparrow-coder").build();


        File directory = new File(frontScaffoldHome);
        FolderFilter folderFilter = (sourceFile) -> {
            String fileName = sourceFile.replace(backendScaffoldHome, "");
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
            //sourceFileName: /{home}/sparrow/sparrow-js/sparrow-coder/package.js
            //scaffoldHome: /{home}/sparrow/sparrow-js/sparrow-coder
            String targetFileName = sourceFileName.replace(frontScaffoldHome, "");
            String targetPath = new FileNameBuilder(config.getProjectHome(projectConfig)).joint(projectConfig.getFrontendName()).joint(targetFileName).build();
            try {
                FileUtility.getInstance().copy(sourceFileName, targetPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        String[] directoryList = directory.list();
        if (directoryList == null || directoryList.length == 0) {
            log.error("directory [{}] is empty", backendScaffoldHome);
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

    private static void copyBackend(TableConfigRegistry registry, Boolean admin) throws IOException {
        ProjectConfigBO projectConfig = registry.getProject().getProjectConfig();
        //如果复制的是admin 则全部脚手架一起复制包括bom pom.xml 如果只是server 则源从admin下的结构开始复制
        String adminPackage = admin ? "" : "admin";
        String scaffoldTemplate = new FileNameBuilder(registry.getProject().getEnvConfig().getBackendScaffoldTemplateHome()).joint("sparrow-example").joint(adminPackage).build();
        File directory = new File(scaffoldTemplate);
        EnvConfig envConfig = registry.getProject().getEnvConfig();
        String[] exampleClazzArray = new String[]{"Department.java", "UserExample.java",};

        FolderFilter backendFolderFilter = (sourceFile) -> {
            //先过滤非法文件夹
            if (sourceFile.contains("target") ||
                    sourceFile.equalsIgnoreCase(".DS_Store") ||
                    sourceFile.contains(".idea")) {
                return true;
            }

            int fileIndex = sourceFile.lastIndexOf(File.separator);
            //不存在/ 说明是正常目录 因为目录过滤不是 full path
            if (fileIndex == -1) {
                return false;
            }
            String fileName = sourceFile.substring(fileIndex + 1);

            if (ArrayUtils.contains(exampleClazzArray, fileName)) {
                return true;
            }
            if (fileName.equalsIgnoreCase(".gitignore")) {
                return false;
            }
            //如果是隐藏文件，跳过
            if (fileName.startsWith(".")) {
                return true;
            }
            return false;
        };
        FileCopier backendFolderCopier = sourceFileName -> {
            //sourceFileName: /{home}/sparrow-example/bom/pom.xml
            //scaffoldHome: /{home}/sparrow-example
            String targetFileName = sourceFileName.replace(scaffoldTemplate, "").replace("example", projectConfig.getModulePrefix());
            String targetPath = null;
            if (admin) {
                targetPath = new FileNameBuilder(envConfig.getProjectHome(projectConfig)).joint(targetFileName).build();
            } else {
                targetFileName = targetFileName.replace("admin-", "");
                targetPath = new FileNameBuilder(envConfig.getProjectHome(projectConfig)).joint("server").joint(targetFileName).build();
            }
            String content = FileUtility.getInstance().readFileContent(sourceFileName);
            content = content.replaceAll("example", projectConfig.getModulePrefix());
            if (!admin) {
                content = content.replaceAll(".admin", "");
            }
            content = content.replace("${base_package}", projectConfig.getFullBasePackageName());
            try {
                FileUtility.getInstance().writeFile(targetPath, content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        String[] directoryList = directory.list();
        if (directoryList == null || directoryList.length == 0) {
            log.error("directory [{}] is empty", scaffoldTemplate);
            return;
        }
        for (String childDirectory : directoryList) {
            //过滤掉不需要的文件夹
            if (backendFolderFilter.filter(childDirectory)) {
                continue;
            }
            String sourceDirectoryPath = new FileNameBuilder(scaffoldTemplate).joint(childDirectory).build();
            FileUtility.getInstance().recurseCopy(sourceDirectoryPath, backendFolderCopier, backendFolderFilter);
        }
        String poPackageFullName = projectConfig.getFullBasePackageName();
        String poPackagePath = ClassPathUtils.packageToPath(poPackageFullName);


        if (!admin) {
            return;
        }
        for (String exampleClazz : exampleClazzArray) {
            String sourceFile = new FileNameBuilder(scaffoldTemplate).joint("example-po").joint("src").joint("main").joint("java").joint("example").joint(exampleClazz).build();
            String sourceContent = FileUtility.getInstance().readFileContent(sourceFile);
            sourceContent = sourceContent.replace("package example;", "package " + projectConfig.getFullBasePackageName() + ".po;");
            String targetFile = new FileNameBuilder(envConfig.getProjectHome(projectConfig)).joint(projectConfig.getModulePrefix() + "-po").joint("src").joint("main").joint("java").joint(poPackagePath).joint("po").joint(exampleClazz).build();
            FileUtility.getInstance().writeFile(targetFile, sourceContent);
        }
    }

    public static void copy(TableConfigRegistry registry) throws IOException {
        copyBackend(registry, true);
        copyBackend(registry, false);
        copyFrontend(registry);
    }
}
