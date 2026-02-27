package com.irfan.ecommerce.ui.tests;

import com.irfan.ecommerce.api.clients.demoblaze.*;
import com.irfan.ecommerce.ui.base.BaseTest;
import com.irfan.ecommerce.ui.pages.CartPage;
import com.irfan.ecommerce.util.PropertyReader;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CartHybridTest: The "Short-Circuit" E2E Validation.
 * 2026-02-27: Fixed property key alignment and added null-safety guards.
 */
public class CartHybridTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(CartHybridTest.class);
    private AuthClient authClient;
    private CartClient cartClient;
    private CartPage cartPage;

   @Test(description = "Verify Cart Total via Hybrid API-UI Injection")
    public void verifyCartTotalWithApiInjection() {
        authClient = new AuthClient();
        cartClient = new CartClient();

        // 1. ACTION: Align keys with config.properties (added demoblaze. prefix)
        String user = PropertyReader.getProperty("demoblaze.username");
        String pass = PropertyReader.getProperty("demoblaze.password");
        String baseUrl = PropertyReader.getProperty("demoblaze.url");

        // 2. DEFENSIVE GUARD: Catch nulls before they crash the API clients
        Assert.assertNotNull(user, "🛑 CONFIG ERROR: 'demoblaze.username' is missing!");
        Assert.assertNotNull(baseUrl, "🛑 CONFIG ERROR: 'demoblaze.url' is missing!");
        
        // 3. API ACTION: Setup state in <1 second
        logger.info("🔐 HYBRID: Fetching API token for: {}", user);
        String token = authClient.getAuthToken(user, pass);

        // Inject "Sony xperia z5" (ID: 6) directly via API
        cartClient.addToCart("6", token);
        logger.info("🛒 HYBRID: Product injected via API. Bypassing 30s of Selenium clicks.");

        // 4. UI ACTION: Synchronize browser session
        // Assuming loginViaApi handles the cookie injection
        loginViaApi(user, pass); 
        
        // Navigate directly to the Cart page
        driver.get(baseUrl + "/cart.html");

        // 5. VALIDATION: Page Object Model
        cartPage = new CartPage(driver);
        String actualProduct = cartPage.getProductName(1);
        
        // RESULT: Validating that the API-injected data is correctly reflected in the UI.
        Assert.assertEquals(actualProduct, "Sony xperia z5", "❌ ERROR: Cart Data Mismatch!");
    }
}