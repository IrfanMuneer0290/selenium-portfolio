package com.irfan.ecommerce.ui.base;

import com.irfan.ecommerce.util.GenericActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

     /**
     * 🛡️ SELF-HEALING WAIT: This handles the String[] arrays from ObjectRepo.
     * It uses your GenericActions logic to find the 'Best' working locator.
     */
     protected void waitForVisibilityOfElement(String[] locatorArray) {
        // This is the CRITICAL fix: getBestLocator converts "id:loginusername" 
        // into a real By.id() so Selenium doesn't think it's an XPath.
        By bestBy = GenericActions.getBestLocator(locatorArray);
        wait.until(ExpectedConditions.visibilityOfElementLocated(bestBy));
    }
    /**
     * 🎯 STANDARD WAIT: This handles standard Selenium 'By' locators.
     * Fixes the "locators cannot be resolved" error.
     */
    protected void waitForVisibilityOfElement(By locator) {
       // ✅ FIX: Use 'locator' (the variable name you defined)
       wait.until(ExpectedConditions.visibilityOfElementLocated(locator)); 
    }
}
