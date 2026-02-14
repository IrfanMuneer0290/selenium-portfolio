package com.irfan.ecommerce.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.URL; // 👈 CRITICAL: Add this import

public class DriverFactory {
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static WebDriver initDriver(String browser) {
        String executionEnv = System.getProperty("execution_env", "local");
        String headless = System.getProperty("headless", "false");
        
        if (browser == null || browser.isEmpty()) {
            browser = "chrome";
        }

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();

            if ("true".equalsIgnoreCase(headless)) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
            }

            try {
                if (executionEnv.equalsIgnoreCase("remote")) {
                    // 🐋 Remote Docker Execution
                    tlDriver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options));
                } else {
                    // 💻 Local Execution
                    WebDriverManager.chromedriver().setup();
                    tlDriver.set(new ChromeDriver(options));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Only maximize if NOT in headless mode to avoid CI crashes
        if (!"true".equalsIgnoreCase(headless) && getDriver() != null) {
            getDriver().manage().window().maximize();
        }
        
        return getDriver();
    }

    public static synchronized WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        if (getDriver() != null) {
            getDriver().quit();
            tlDriver.remove();
        }
    }
}
