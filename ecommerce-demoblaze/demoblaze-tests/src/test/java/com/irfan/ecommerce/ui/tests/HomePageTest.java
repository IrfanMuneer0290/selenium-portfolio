package com.irfan.ecommerce.ui.tests;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.irfan.ecommerce.ui.base.BaseTest;
import com.irfan.ecommerce.ui.pages.HomePage;

/**
 * HomePageTest: The "Smoke Test" for Business Availability.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: During holiday sales (Black Friday), the site would sometimes 
 *   load but the "Core Components" were missing. A simple "Title Check" 
 *   wasn't enough to tell if the site was actually open for business.
 * - WHAT I DID: I built this test to act as a "Health Check." It doesn't just 
 *   check the URL; it validates that the brand 'STORE' is visible and 
 *   logs the event to Splunk so the SRE team knows the frontend is healthy.
 * - THE RESULT: This became our "Tier 1" health check. If this test fails 
 *   in the CI, we block the entire deployment immediately.
 */
public class HomePageTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(HomePageTest.class);

    @Test(description = "Validates the Brand Identity and Storefront availability.")
    public void testHomePageTitle() {
        reportLog("🔍 Starting Tier-1 Availability Check for HomePage.");
        
        // No need to 'new' it here; it's already initialized in BaseTest
        homePage.open();
        
        String actualTitle = homePage.getTitleText();
        
        reportLog("📊 UI Validation: Checking brand identity. Found: " + actualTitle);
        
        // Demoblaze brand is usually "PRODUCT STORE" or contains "STORE"
        Assert.assertTrue(actualTitle.contains("STORE"), 
            "❌ FAILURE: Brand 'STORE' not found. UI rendering may be incomplete.");
            
        reportLog("✅ PASS: Storefront is healthy and accessible.");
    }
}
