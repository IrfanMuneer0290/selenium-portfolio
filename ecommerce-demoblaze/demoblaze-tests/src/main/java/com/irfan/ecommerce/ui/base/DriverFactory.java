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
        logger.info("🔧 Thread [{}] initDriver({})", Thread.currentThread().getId(), browserName);
        
        // CRITICAL: Remove old driver first
        quitDriver();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        
        try {
            WebDriverManager.chromedriver().setup();
            WebDriver driver = new ChromeDriver(options);
            tlDriver.set(driver);  // THREAD-SAFE SET
            allDrivers.add(driver);
            
            logger.info("✅ Thread [{}] driver created: {}", Thread.currentThread().getId(), driver);
            return driver;
            
        } catch (Exception e) {
            logger.error("❌ Thread [{}] driver creation FAILED", Thread.currentThread().getId(), e);
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
