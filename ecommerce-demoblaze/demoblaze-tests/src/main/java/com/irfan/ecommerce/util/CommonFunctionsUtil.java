package com.irfan.ecommerce.util;

import java.io.InputStream;
import java.util.Properties;

public class CommonFunctionsUtil {
    private static final Properties prop = new Properties();
    
    static {
        try (InputStream input = CommonFunctionsUtil.class
                .getClassLoader().getResourceAsStream("webelement.properties")) {
            if (input == null) {
                throw new RuntimeException("webelement.properties NOT FOUND");
            }
            prop.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Properties load FAILED", e);
        }
    }
    
}
