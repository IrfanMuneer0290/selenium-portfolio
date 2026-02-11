package com.irfan.ecommerce.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {
    private static WebDriver driver;
    
    public static WebDriver initDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }
    
    public static WebDriver getDriver() {
        return driver;
    }
    
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
