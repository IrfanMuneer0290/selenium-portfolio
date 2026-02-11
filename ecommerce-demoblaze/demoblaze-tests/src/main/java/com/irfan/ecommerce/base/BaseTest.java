package com.irfan.ecommerce.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    public WebDriver driver;
    
    @BeforeMethod
    public void setup() {
        driver = DriverFactory.initDriver(); 
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.demoblaze.com/");
    }
    
    @AfterMethod
    public void teardown() {
        if (driver != null) driver.quit();
    }
}
