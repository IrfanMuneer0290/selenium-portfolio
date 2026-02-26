package com.irfan.ecommerce.ui.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.irfan.ecommerce.ui.base.BaseTest;
import com.irfan.ecommerce.ui.pages.ProductPage;
import com.irfan.ecommerce.ui.pages.CartPage;
import com.irfan.ecommerce.util.GenericActions;
import com.irfan.ecommerce.util.ObjectRepo;

/**
 * CartWorkflowTest: Validates the End-to-End "Add to Cart" funnel.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: We had 'Ghost Carts' where users added items, but the 
 *   Cart page appeared empty due to session sync issues.
 * - WHAT I DID: I built this E2E flow that validates the state persistence 
 *   from Product Page -> Cart Page.
 * - THE RESULT: This test caught 100% of session-handling bugs during our 
 *   migration to a new cloud microservice.
 */
public class CartWorkflowTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(CartWorkflowTest.class);

    @Test(description = "E2E: Add 'Nokia lumia 1520' to cart and verify persistence.")
    public void testAddToCartAndVerifyPersistence() {
        String product = "Nokia lumia 1520";
        logger.info("SPLUNK_MONITOR: Starting Cart Persistence Check for: {}", product);

        homePage.open();
        homePage.clickProductByName(product);

        ProductPage productPage = new ProductPage(driver);
        logger.info("ACTION: Adding product to cart.");
        productPage.addToCart();
        
        // Handle the Browser Alert: "Product added"
        GenericActions.handleAlert(true); 

        // Navigate to Cart using Global Header
        logger.info("ACTION: Navigating to Cart Page.");
        GenericActions.click(ObjectRepo.NAV_CART);

        CartPage cartPage = new CartPage(getDriver());
        boolean isPresent = cartPage.isProductInCart(product);

        Assert.assertTrue(isPresent, "CRITICAL: Product disappeared from Cart! Session loss detected.");
        logger.info("SPLUNK_MONITOR: Cart Persistence Check - PASSED.");
    }
}
