package com.irfan.ecommerce.ui.pages;

import com.irfan.ecommerce.ui.base.BasePage;
import com.irfan.ecommerce.util.GenericActions;
import com.irfan.ecommerce.util.ObjectRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage: The "Identity Controller."
 * 
 * 🚀 THE WALMART-SCALE "WHY" (Resilience Engineering):
 * SITUATION: Modals are notoriously flaky. If you try to sendKeys before 
 *   the 'Login' popup finishes its animation, the test crashes.
 * ACTION: Extending BasePage to inherit 'waitForVisibilityOfElement'. 
 *   We now wait for the Modal fields before interacting.
 */
public class LoginPage extends BasePage {
    
    private static final Logger log = LogManager.getLogger(LoginPage.class);

    public LoginPage(WebDriver driver) {
        super(driver); // IMPACT: Anchors this page to the BasePage Engine
    }

    public LoginPage performLogin(String username, String password) {
        log.info("🚀 START: Commencing Login flow for user: [{}]", username);
        
        try {
            GenericActions.click(ObjectRepo.LOGIN_LINK);
            
            // WALMART MOVE: Wait for the specific Modal field to be visible 
            // before trying to type. This kills 'ElementNotInteractable' bugs.
            waitForVisibilityOfElement(ObjectRepo.LOGIN_USER);
            
            GenericActions.sendKeys(ObjectRepo.LOGIN_USER, username);
            GenericActions.sendKeys(ObjectRepo.LOGIN_PASS, password);
            GenericActions.click(ObjectRepo.LOGIN_BTN);
            
            log.info("✅ END: Login credentials submitted for: [{}]", username);
        } catch (Exception e) {
            log.error("❌ FAILURE: Login flow interrupted. MTTR Trace: {}", e.getMessage());
            throw e;
        }
        return this;
    }
}
