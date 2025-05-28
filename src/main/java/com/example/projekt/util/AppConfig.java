package com.example.projekt.util;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties prop = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("config.properties") ) {
            prop.load(input);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
