package com.irfan.ecommerce.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.irfan.ecommerce.base.BaseTest;
import com.irfan.ecommerce.base.HomePage;

public class HomePageTest extends BaseTest {

     private static final Logger logger = LogManager.getLogger(HomePageTest.class);
    @Test
    public void testHomePageTitle() {
         logger.info("SPLUNK TEST: Home Page Test is running!");
        HomePage homePage = new HomePage(driver); 
        homePage.open();
        String actualTitle = homePage.getTitleText();
        Assert.assertTrue(actualTitle.contains("STORE"));
    }
}
