package com.sparrow.coder.support.utils;

import com.sparrow.utility.StringUtility;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

@Slf4j
public class ConfigUtils {
    public static Properties initPropertyConfig(String configContent) throws IOException {
        Properties config = new Properties();
        if (!StringUtility.isNullOrEmpty(configContent)) {
            log.info("load config from file: " + configContent);
            config.load(new StringReader(configContent));
            return config;
        }
        InputStream configStream = ConfigUtils.class.getResourceAsStream("/config.properties");
        config.load(configStream);
        return config;
    }
}
