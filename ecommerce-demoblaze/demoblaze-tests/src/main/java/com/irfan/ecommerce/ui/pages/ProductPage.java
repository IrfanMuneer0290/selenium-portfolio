package com.irfan.ecommerce.ui.pages;

import org.openqa.selenium.WebDriver;

import com.irfan.ecommerce.util.GenericActions;
import com.irfan.ecommerce.util.ObjectRepo;

public class ProductPage {

    // Even if we use GenericActions, we keep this constructor 
    // so the Test classes don't break when passing 'driver'.
    public ProductPage(WebDriver driver) {
        // No need to do this.driver = driver; if only using GenericActions
    }

    public String getProductName() {
        // Uses the locator from ObjectRepo
        return GenericActions.getText(ObjectRepo.PRODUCT_TITLE);
    }

    public String getProductPrice() {
        // Uses the new locator we just added
        return GenericActions.getText(ObjectRepo.PRODUCT_PRICE);
    }

    public void addToCart() {
        GenericActions.click(ObjectRepo.ADD_TO_CART_BTN);
    }
}
