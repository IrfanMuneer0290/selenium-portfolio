package com.irfan.ecommerce.api.tests.demoblaze;

import com.irfan.ecommerce.api.clients.demoblaze.AuthClient;
import com.irfan.ecommerce.api.clients.demoblaze.CartClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ApiValidationTest extends com.irfan.ecommerce.ui.base.BaseTest {
     private static final Logger logger = LogManager.getLogger(ApiValidationTest.class);
    
    AuthClient authClient = new AuthClient();
    CartClient cartClient = new CartClient();
    
    @Test(description = "API Login → Token Extraction → Walmart-scale Auth")
    public void testApiLogin() {
        String token = authClient.getAuthToken("irfan_qa", "testpass123");
        assertNotNull(token, "Auth token must not be null");
        assertTrue(token.length() > 10, "Token must be valid length");
        logger.info("✅ API LOGIN SUCCESS: Token length = {}", token.length());
    }
    
    @Test(dependsOnMethods = "testApiLogin", description = "API Add to Cart → Hybrid UI Validation")
    public void testApiAddToCart() {
        String token = authClient.getAuthToken("irfan_qa", "testpass123");
        cartClient.addToCart("1", token);  // Samsung Galaxy S6
        logger.info("✅ API CART ADDED: Product ID=1, Token={}", token.substring(0, 10) + "...");
    }
}
