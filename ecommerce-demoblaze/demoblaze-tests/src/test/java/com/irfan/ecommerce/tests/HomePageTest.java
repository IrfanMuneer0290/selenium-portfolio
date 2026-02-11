package com.irfan.ecommerce.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.irfan.ecommerce.base.BaseTest;
import com.irfan.ecommerce.base.HomePage;

public class HomePageTest extends BaseTest {
    @Test
    public void testHomePageTitle() {
        HomePage homePage = new HomePage(driver); 
        homePage.open();
        String actualTitle = homePage.getTitleText();
        Assert.assertTrue(actualTitle.contains("STORE"));
    }
}
