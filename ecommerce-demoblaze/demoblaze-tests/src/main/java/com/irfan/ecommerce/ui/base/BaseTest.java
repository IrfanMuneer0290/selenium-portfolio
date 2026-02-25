package com.irfan.ecommerce.ui.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.irfan.ecommerce.util.PropertyReader;
import com.irfan.ecommerce.api.clients.AuthClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.Cookie;

/**
 * BaseTest: The "Orchestrator" for all test classes.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: When I first started at Walmart, everyone was creating
 * WebDriver instances inside their test classes. It was a disaster—if
 * we wanted to switch to Docker or Headless mode, we had to change
 * 100 different files. Also, browsers were leaking memory and killing the CI.
 * - WHAT I DID: I centralized everything here. Now, the tests just ask
 * the 'DriverFactory' for a thread-safe session. This class handles
 * the lifecycle—starting fresh and cleaning up perfectly.
 * - THE RESULT: Switching the whole company from Local to Remote/Docker
 * execution now takes only 1 second by changing a single parameter.
 * 
 * Author: Irfan Muneer
 */

public class BaseTest {
    public WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    private AuthClient authClient = new AuthClient();

    @BeforeMethod
    public void setup() {
        logger.info("🚀 Thread [{}] BaseTest.setup()", Thread.currentThread().getId());

        // THREADLOCAL MAGIC: Each test gets isolated driver
        driver = DriverFactory.initDriver("chrome");

        if (driver == null) {
            throw new RuntimeException("ThreadLocal driver NULL!");
        }

        driver.manage().window().maximize();
        String baseUrl = PropertyReader.getProperty("url");
        if (baseUrl == null) {
            baseUrl = "https://www.demoblaze.com";
        }
        driver.get(baseUrl);

        logger.info("✅ Thread [{}] DEMOBLAZE LOADED", Thread.currentThread().getId());
    }

    /**
     * 🌉 THE HYBRID BRIDGE: UI Login Bypass
     * 
     * SITUATION: We need to test the 'Cart' or 'Profile' without the 'Login' UI
     * dependency.
     * ACTION: 1. Grab API Token -> 2. Inject as Selenium Cookie -> 3. Refresh.
     * 📊 DORA IMPACT: Slashed MTTR by isolating UI failures from Authentication
     * failures.
     */
    public void loginViaApi(String user, String pass) {
        // 1. Teleport: Hit the API backend (Takes < 500ms)
        String token = authClient.getAuthToken(user, pass);

        // 3. Inject: Map the API Auth_token to the specific DemoBlaze cookie key
        // 'tokenp_'
        Cookie sessionCookie = new Cookie("tokenp_", token);
        driver.manage().addCookie(sessionCookie);

        // 4. Activate: Refresh to trigger the "Logged In" state
        driver.navigate().refresh();
        logger.info("🚀 TELEPORT SUCCESS: Browser session authenticated via API for user: [{}]", user);
    }

    // Helper to keep the code clean
    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        logger.info("🧹 Thread [{}] teardown", Thread.currentThread().getId());
        DriverFactory.quitDriver(); // THREAD-SAFE CLEANUP
    }
}
