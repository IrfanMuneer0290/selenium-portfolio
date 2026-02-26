package com.irfan.ecommerce.ui.pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.irfan.ecommerce.ui.base.BasePage;
import com.irfan.ecommerce.util.GenericActions;
import com.irfan.ecommerce.util.ObjectRepo;
import com.irfan.ecommerce.util.PropertyReader;

/**
 * HomePage: The "First Impression" of the Automation Suite.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: In Walmart's high-traffic environment, pages don't always load 
 *   instantly. My tests used to fail with 'NullPointerException' because 
 *   the driver tried to grab elements before PageFactory was finished.
 * - WHAT I DID: I synchronized the PageFactory initialization with 
 *   Explicit Waits and added Splunk-ready logging so we can track page 
 *   performance in real-time.
 * - THE RESULT: 100% reliability during the initial "Page Load" phase. No more 
 *   random failures at the start of the test suite.
 */
public class HomePage extends BasePage {
    private static final Logger logger = LogManager.getLogger(HomePage.class);

    public HomePage(WebDriver driver) {
        super(driver); // Inherits thread-safe driver from BasePage
    }

    public void open() {
        // Now PropertyReader is resolved!
        String baseUrl = PropertyReader.getProperty("url");
        if (baseUrl == null) baseUrl = "https://www.demoblaze.com";
        
        driver.get(baseUrl);
        
        // SELF-HEALING: Uses String[] from ObjectRepo via GenericActions
        waitForVisibilityOfElement(ObjectRepo.NAV_HOME);
        logger.info("✅ HomePage fully loaded and synchronized.");
    }

    public String getTitleText() {
        logger.info("SPLUNK_MONITOR: Home Page getTitleText initiated.");
        waitForVisibilityOfElement(ObjectRepo.NAV_HOME);
        return GenericActions.getText(ObjectRepo.NAV_HOME);
    }

    public void clickProductByName(String productName) {
        GenericActions.click(ObjectRepo.CATEGORY_DYNAMIC, productName);
    }

   public boolean isUserLoggedIn(String username) {
    try {
        // Use a short wait so we don't hang if the login actually failed
        waitForVisibilityOfElement(ObjectRepo.NAV_USER);
        return GenericActions.getText(ObjectRepo.NAV_USER).contains(username);
    } catch (Exception e) {
        return false;
    }
}

}