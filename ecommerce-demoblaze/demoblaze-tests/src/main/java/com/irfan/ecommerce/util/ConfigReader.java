package com.irfan.ecommerce.util;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * ConfigReader: Handles Environment Orchestration.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: Even with Maven variables, if the Java code doesn't know how 
 *   to map "-Denv=prod" to "prod.properties", the build fails. 
 * - WHAT I DID: I built this dynamic loader. It catches the Maven variable 
 *   at runtime and loads the specific property file for that environment.
 * - THE RESULT: 100% Environment Portability. The same artifact runs in 
 *   Dev, QA, and Prod without a single line of code change.
 */
public class ConfigReader {
    private static Properties prop;

    static {
        try {
            prop = new Properties();
            // 1. Get the environment from Maven command line, default to 'qa'
            String env = System.getProperty("env", "qa"); 
            
            // 2. Build the path to your specific folder structure
            String configPath = "src/main/resources/config/" + env + ".properties";
            
            FileInputStream fis = new FileInputStream(configPath);
            prop.load(fis);
            System.out.println("CONFIG: Successfully loaded " + env + " environment settings.");
        } catch (IOException e) {
            throw new RuntimeException("FATAL: Could not find config file at src/main/resources/config/! " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
