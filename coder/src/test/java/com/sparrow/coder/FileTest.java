package com.sparrow.coder;

import java.io.File;
import java.nio.file.Path;

public class FileTest {
    public static void main(String[] args) {
        System.out.println(File.separator);
        System.out.println(File.pathSeparator);
        System.out.println(System.getProperty("user.home"));
    }
}
