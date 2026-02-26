package com.irfan.ecommerce.ui.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

     // 🚀 THE GLOBAL REGISTRY: Tracks every driver instance across ALL threads
    private static final Set<WebDriver> allDrivers = Collections.synchronizedSet(new HashSet<>());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.warn("🚨 JVM Shutdown: Commencing 100% Resource Cleanup for {} active drivers...", allDrivers.size());
            
            // Synchronized block ensures no new drivers are added while we are killing them
            synchronized (allDrivers) {
                for (WebDriver driver : allDrivers) {
                    try {
                        if (driver != null) {
                            driver.quit();
                            logger.info("✅ Force-killed orphaned driver instance: {}", driver);
                        }
                    } catch (Exception e) {
                        logger.error("❌ Failed to kill a ghost driver: {}", e.getMessage());
                    }
                }
                allDrivers.clear();
            }
        }));
    }


    public static WebDriver initDriver(String browserName) {
    String env = System.getProperty("execution_env", "local"); // Default to local
    logger.info("🔧 Thread [{}] Environment: {} | Browser: {}", Thread.currentThread().getId(), env, browserName);

    quitDriver();
    
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--remote-allow-origins=*");
    
    // 🔥 WALMART CLOUD STRATEGY: Headless for CI performance
    boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
    if (isHeadless) {
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
    }

    try {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(options);
        tlDriver.set(driver);
        allDrivers.add(driver);
        return driver;
    } catch (Exception e) {
        logger.error("❌ Driver initialization failed on host: {}", System.getProperty("os.name"));
        throw new RuntimeException("Driver init failed", e);
    }
}

    public static WebDriver getDriver() {
        WebDriver driver = tlDriver.get();
        if (driver == null) {
            logger.warn("⚠️ Thread [{}] driver NULL - forcing init", Thread.currentThread().getId());
            return initDriver("chrome");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            allDrivers.remove(driver);
            driver.quit();
            tlDriver.remove();
            logger.info("🧹 Thread [{}] driver quit + ThreadLocal cleared", Thread.currentThread().getId());
        }
    }
}
