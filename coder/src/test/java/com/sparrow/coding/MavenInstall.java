package com.sparrow.coding;

import com.sparrow.protocol.constant.Constant;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.FileUtility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import sun.misc.JarFilter;

public class MavenInstall {

    private static Runtime _runRuntime = Runtime.getRuntime();
    private static String CMD_INSTALL_FILE;

    public static void main(String[] args) {
        File file = new File(EnvironmentSupport.getInstance().getWorkspace() + "/Sparrow/jar");
        FilenameFilter filter = new JarFilter();
        File[] jarFiles = file.listFiles(filter);
        for (File jar : jarFiles) {
            installJarToMaven(jar);
        }
    }

    private static void installJarToMaven(File file) {
        String fileName = file.getName();
        String jarName = FileUtility.getInstance().getFileNameProperty(fileName).getName();
        StringTokenizer strToken = new StringTokenizer(jarName, "-");
        String groupId = null;
        String artifactId = null;
        String version = null;
        if (strToken.hasMoreTokens()) {
            groupId = strToken.nextToken();
            if (strToken.hasMoreTokens()) {
                artifactId = strToken.nextToken();
                if (strToken.hasMoreTokens()) {
                    version = strToken.nextToken();
                }
            } else {
                version = artifactId = groupId;
            }
        }
        System.out.println("Jar [" + jarName + "] will be installed with the groupId=" + groupId + " ,"
            + "artifactId=" + artifactId + " , version=" + version + ".");
        executeInstall(groupId, artifactId, version, file.getPath());
    }

    private static void executeInstall(String groupId, String artifactId,
        String version, String path) {
        CMD_INSTALL_FILE = createInstallFileCMD(groupId, artifactId,
            version, path);
        String[] cmdArray = new String[] {"cmd", "/C", CMD_INSTALL_FILE};
        try {
            Process process = _runRuntime.exec(cmdArray);
            printResult(process);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printResult(Process process) throws IOException {
        InputStream is = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Constant.CHARSET_UTF_8));
        String lineStr;
        while ((lineStr = br.readLine()) != null) {
            System.out.println(lineStr);
        }
    }

    private static String createInstallFileCMD(String groupId,
        String artifactId, String version, String path) {
        StringBuilder sb = new StringBuilder();
        sb.append("mvn install:install-file -DgroupId=").append(groupId)
            .append(" -DartifactId=").append(artifactId)
            .append(" -Dversion=").append(version)
            .append(" -Dpackaging=jar")
            .append(" -Dfile=").append(path);
        return sb.toString();
    }
}
