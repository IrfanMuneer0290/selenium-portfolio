package com.irfan.ecommerce.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.irfan.ecommerce.base.DriverFactory;

public class GenericActions {

    // Generic method to get a Wait instance with a default timeout
    private static WebDriverWait getWait() {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));
    }

    // Overloaded method if you need a specific custom timeout
    private static WebDriverWait getWait(int seconds) {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(seconds));
    }

    public static void clickWebElement(WebElement element)
    {
        try {
            element.click();
            
        } catch (Exception e) {
            System.err.println("FAILED to click WebElement: " + e.getMessage());
        }
    }

    /*For clickLocator: Your current code is fine for a single-threaded test.
     If you later decide to run tests in parallel, 
      must ensure DriverFactory uses a ThreadLocal<WebDriver> so that getDriver() 
    returns the correct browser for the specific test running that code. Selenium Documentation: ThreadGuard.
 */

        // Robust Click with Generic Wait
    public static void clickLocator(By locator) {
        try {
            getWait().until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            System.err.println("FAILED to click Locator [" + locator + "]: " + e.getMessage());
        }
    }

    // Robust SendKeys with Generic Wait
    public static void sendKeys(By locator, String text) {
        try {
            WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            System.err.println("FAILED to send keys to [" + locator + "]: " + e.getMessage());
        }
    }
    

}
