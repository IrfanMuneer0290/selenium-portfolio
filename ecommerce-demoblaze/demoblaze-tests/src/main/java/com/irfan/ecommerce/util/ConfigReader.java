package com.irfan.ecommerce.util;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

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
            String env = System.getProperty("env", "qa"); 
            
            // ✅ FIXED: Use ClassLoader for Maven/JAR compatibility
            String resourcePath = "config/" + env + ".properties";
            InputStream is = ConfigReader.class.getClassLoader()
                .getResourceAsStream(resourcePath);
                
            if (is == null) {
                throw new RuntimeException("❌ config/" + env + ".properties not found in classpath!");
            }
            
            prop.load(is);
            System.out.println("✅ CONFIG: Loaded config/" + env + ".properties");
        } catch (IOException e) {
            throw new RuntimeException("FATAL: Could not load config properties! " + e.getMessage(), e);
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}