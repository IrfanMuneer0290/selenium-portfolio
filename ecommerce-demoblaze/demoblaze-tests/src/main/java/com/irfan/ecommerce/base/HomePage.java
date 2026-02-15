package com.irfan.ecommerce.base;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
     private static final Logger logger = LogManager.getLogger(HomePage.class);
    private WebDriver driver = DriverFactory.getDriver();
    private final String URL = "https://www.demoblaze.com/";
    
     public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // FIXED LOCATOR - Demoblaze uses this
    @FindBy(xpath = "//a[contains(text(),'Phones')]") 
    private WebElement phonesCategory;

    @FindBy(id = "nava")
    private WebElement storeTitle;
    
    public HomePage() {
        PageFactory.initElements(driver, this);
    }
    
    public void open() {
        driver.get(URL);
    }

    public String getTitleText() {
         logger.info("SPLUNK TEST: Home Page getTitleText!");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(storeTitle));
        return storeTitle.getText();
    }
    
    public String getPhonesCategoryText() {
    return phonesCategory.getText();
    }

}
