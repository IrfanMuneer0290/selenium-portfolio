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

    // 🛡️ THE FIX: This allows your Pages to pass String[] from ObjectRepo
    protected void waitForVisibilityOfElement(String[] locatorArray) {
        By bestBy = GenericActions.getBestLocator(locatorArray);
        wait.until(ExpectedConditions.visibilityOfElementLocated(bestBy));
    }

    protected void waitForVisibilityOfElement(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
