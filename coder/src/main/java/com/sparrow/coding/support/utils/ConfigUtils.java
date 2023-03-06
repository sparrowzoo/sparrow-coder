package com.sparrow.coding.support.utils;

import com.sparrow.coding.config.EnvConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {
    public static Properties initPropertyConfig() throws IOException {
        String sparrowConfig = System.getenv(EnvConfig.SPARROW_CONFIG);
        File configFile = new File(sparrowConfig);
        if (!configFile.exists()) {
            System.err.println("template config file can't read");
            System.exit(0);
        }
        InputStream configStream = new FileInputStream(configFile);
        Properties config = new Properties();
        config.load(configStream);
        return config;
    }
}
