package com.sparrow.coding;

import com.sparrow.utility.FileUtility;
import java.io.File;

public class ProjectCopier {
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

    //复制文件夹,循环遍历文件的变式使用
    public static void copyFolder(String sourceFolder, String targetFolder) {
        File srcF = new File(sourceFolder);
        //获取目录下的所有文件
        File[] files = srcF.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                //也可以转换为字符串，进行字符串的替换操作
                String target = targetFolder + "/" + f.getName();
                FileUtility.getInstance().copy(f.getAbsolutePath(), target);
            } else if (f.isDirectory()) {
                //准备进行目录的创建
                File mkFile = new File(targetFolder + "/" + f.getName());
                if (!mkFile.exists()) {
                    //先将目录创建出来
                    mkFile.mkdir();
                }
                //递归调用
                copyFolder(f.toString(), targetFolder + "/" + f.getName());
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
                copyFolder(sourceDirectoryPath, target);
            }
        }
    }
}
