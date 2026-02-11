package com.irfan.ecommerce.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.irfan.ecommerce.base.DriverFactory;

public class HomePage {
    private WebDriver driver = DriverFactory.getDriver();
    private final String URL = "https://www.demoblaze.com/";
    
    // FIXED LOCATOR - Demoblaze uses this
    @FindBy(xpath = "//a[contains(text(),'Phones')]") 
    private WebElement phonesCategory;
    
    public HomePage() {
        PageFactory.initElements(driver, this);
    }
    
    public void open() {
        driver.get(URL);
    }
    
    public String getPhonesCategoryText() {
    return phonesCategory.getText();
    }

}
