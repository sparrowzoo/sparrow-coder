package com.sparrow.coding;

import com.sparrow.utility.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class ProjectCopier {
    private static Logger logger = LoggerFactory.getLogger(ProjectCopier.class);

    private static String workspace;
    private static String target;
    private static String resourceTarget;

    public static void run(String config) throws IOException {
        Properties properties = System.getProperties();
        properties.load(ProjectCopier.class.getResourceAsStream("/tedu/" + config));

        String userHome = System.getProperty("user.home");
        workspace = properties.getProperty("workspace");
        workspace = workspace.replace("${user.home}", userHome);

        String sources = properties.getProperty("sources");
        String[] sourceProjects = sources.split(",");
        target = workspace + properties.getProperty("target");
        resourceTarget = workspace + properties.getProperty("resource_target");
        for (String source : sourceProjects) {
            try {
                copy(workspace + source);
            } catch (Exception e) {
                System.out.println(source + " is error");
            }
        }
    }

    public static void main(String[] args2) throws IOException {
        run("sparrow_copier.properties");
        //run("copier.properties");
        //run("admin_copier.properties");
    }

    private static FileUtility.FolderFilter filter = (sourceFile, targetFile) -> {
        if (sourceFile.equalsIgnoreCase("target")) {
            return true;
        }
        //main 排除
        if (sourceFile.startsWith(".")) {
            return true;
        }
        if (sourceFile.endsWith("-main")) {
            return true;
        }
        return false;
    };

    private static void copy(String source) {
        File directory = new File(source);
        String[] directoryList = directory.list();
        if (directoryList == null) {
            return;
        }
        for (String childDirectory : directoryList) {
            if (filter.filter(childDirectory, target)) {
                continue;
            }

            if (childDirectory.contains("dao-impl")) {
                String mapperSource = source + File.separator + childDirectory + "/src/main/resources";
                File sourceDirectory = new File(mapperSource);
                if (sourceDirectory.exists()) {
                    FileUtility.getInstance().recurseCopy(mapperSource, resourceTarget);
                } else {
                    logger.warn("{} is not exist", mapperSource);
                }
                continue;
            }


            String sourceDirectoryPath = source + File.separator + childDirectory + "/src/main/java";
            File sourceDirectory = new File(sourceDirectoryPath);
            if (sourceDirectory.exists()) {
                logger.info("copy {} to {}", sourceDirectoryPath, target);
                FileUtility.getInstance().recurseCopy(sourceDirectoryPath, target);
            } else {
                logger.warn("{} is not exist", sourceDirectoryPath);
            }
        }
    }
}
