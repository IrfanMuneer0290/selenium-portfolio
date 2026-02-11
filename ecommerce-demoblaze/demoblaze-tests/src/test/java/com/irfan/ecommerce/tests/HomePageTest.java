package com.irfan.ecommerce.tests;

import com.irfan.ecommerce.base.BaseTest;
import com.irfan.ecommerce.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {
    
    @Test
    public void testPhonesCategory() {
        HomePage homePage = new HomePage();
        homePage.open();
        
        String actualText = homePage.getPhonesCategoryText();
        Assert.assertTrue(actualText.contains("Phones"), "Phones category not found");
    }
}
