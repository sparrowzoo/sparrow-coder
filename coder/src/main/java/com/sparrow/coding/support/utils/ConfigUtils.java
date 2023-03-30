package com.sparrow.coding.support.utils;

import com.sparrow.utility.StringUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {
    public static Properties initPropertyConfig(String sparrowConfig) throws IOException {
        InputStream configStream = null;
        if (StringUtility.isNullOrEmpty(sparrowConfig) || "default".equalsIgnoreCase(sparrowConfig)) {
            configStream = Class.class.getResourceAsStream("/config.properties");
        } else {
            File configFile = new File(sparrowConfig);
            if (!configFile.exists()) {
                System.err.println("template config file can't read");
                System.exit(0);
            }
            configStream = new FileInputStream(configFile);
        }
        Properties config = new Properties();
        config.load(configStream);
        return config;
    }
}
