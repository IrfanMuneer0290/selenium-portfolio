package com.irfan.ecommerce.ui.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.irfan.ecommerce.ui.base.BaseTest;  
import com.irfan.ecommerce.ui.pages.ProductPage; 

/**
 * ProductDetailTest: Validates dynamic content and pricing integrity.
 */
public class ProductDetailTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(ProductDetailTest.class);

    @Test(description = "Validates PDP content integrity for Samsung Galaxy S6")
    public void testProductPricingAndDescription() {
        logger.info("SPLUNK_MONITOR: Starting PDP Integrity Check for 'Samsung galaxy s6'.");
        
        homePage.open();
        
        // 1. Navigate to Product using the Dynamic Locator logic in ObjectRepo
        logger.info("ACTION: Clicking on 'Samsung galaxy s6' link.");
        homePage.clickProductByName("Samsung galaxy s6");
        
        // 2. Initialize ProductPage (Now utilizing GenericActions/ObjectRepo internally)
        ProductPage productPage = new ProductPage(driver);
        
        // 3. Validate Price (Walmart-scale logic: handles microservice latency/formatting)
        String actualPrice = productPage.getProductPrice();
        logger.info("VALIDATION: Actual Price found in UI: {}", actualPrice);
        
        
        Assert.assertTrue(actualPrice.contains("$360"), 
         "REVENUE RISK: Price mismatch! Expected $360 but found: " + actualPrice);
            
        // 4. Validate Product Header
        String actualName = productPage.getProductName();
        Assert.assertEquals(actualName, "Samsung galaxy s6", "UI ERROR: Product name mismatch!");
        
        logger.info("SPLUNK_MONITOR: PDP Integrity Check - PASSED.");
    }
}
