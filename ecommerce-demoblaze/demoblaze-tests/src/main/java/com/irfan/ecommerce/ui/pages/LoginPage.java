package com.irfan.ecommerce.ui.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.irfan.ecommerce.util.GenericActions;
import com.irfan.ecommerce.util.ObjectRepo;

/**
 * LoginPage: Encapsulates the Login Modal interactions.
 * This class follows the 'Pure POM' rule: No Selenium API leaks into the Page Object.
 */
public class LoginPage {
    
    private static final Logger log = LogManager.getLogger(LoginPage.class);
    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Executes the full Login journey on DemoBlaze.
     * Uses self-healing locators from the ObjectRepo.
     * 
     * @param username - Valid/Invalid test user
     * @param password - Associated password
     * @return LoginPage - Returns this for Fluent Interface / Method Chaining
     */
    public LoginPage performLogin(String username, String password) {
        log.info("START: Commencing Login flow for user: {}", username);
        
        try {
            // 1. Open the Login Modal
            GenericActions.click(ObjectRepo.LOGIN_LINK);
            
            // 2. Input Credentials (Handled by Self-Healing Engine)
            GenericActions.sendKeys(ObjectRepo.LOGIN_USER, username);
            GenericActions.sendKeys(ObjectRepo.LOGIN_PASS, password);
            
            // 3. Submit Login
            GenericActions.click(ObjectRepo.LOGIN_BTN);
            
            log.info("END: Login credentials submitted for: {}", username);
        } catch (Exception e) {
            log.error("BUSINESS_FAILURE: The Login flow was interrupted. Trace: {}", e.getMessage());
            throw e; // Re-throw to fail the TestNG test
        }
        return this;
    }

    /**
     * Specifically handles the JavaScript Alerts that appear on DemoBlaze
     * for 'Wrong Password' or 'User does not exist'.
     */
    public void handleLoginAlert() {
        log.info("ACTION: Checking for Login-related JS Alerts...");
        GenericActions.handleAlert(true); // true = accept/click OK
    }
}
