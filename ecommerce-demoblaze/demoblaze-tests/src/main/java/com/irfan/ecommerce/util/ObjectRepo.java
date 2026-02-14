package com.irfan.ecommerce.util;

/**
 * ObjectRepo: The Centralized Address Book of the Framework.
 * Uses Priority Arrays to support Self-Healing (ID > XPath > CSS).
 * Ye
 */
public class ObjectRepo {

    // --- NAVIGATION & HEADER ---
    public static final String[] NAV_HOME = {"id:nava", "xpath://a[contains(text(),'Home')]"};
    public static final String[] NAV_CONTACT = {"xpath://a[text()='Contact']", "css:a[data-target='#exampleModal']"};
    public static final String[] NAV_CART = {"id:cartur", "xpath://a[text()='Cart']"};
    
    // --- LOGIN MODAL ---
    public static final String[] LOGIN_LINK = {"id:login2", "xpath://a[text()='Log in']"};
    public static final String[] LOGIN_USER = {"id:loginusername", "xpath://input[@id='loginusername']"};
    public static final String[] LOGIN_PASS = {"id:loginpassword", "xpath://input[@id='loginpassword']"};
    public static final String[] LOGIN_BTN = {"xpath://button[text()='Log in']", "css:button.btn-primary"};

    // --- SIGN UP MODAL ---
    public static final String[] SIGNUP_LINK = {"id:signin2", "xpath://a[text()='Sign up']"};
    public static final String[] SIGNUP_USER = {"id:sign-username"};
    public static final String[] SIGNUP_PASS = {"id:sign-password"};
    public static final String[] SIGNUP_BTN = {"xpath://button[text()='Sign up']"};

    // --- CATEGORIES (Dynamic Templates) ---
    // %s will be replaced by "Phones", "Laptops", or "Monitors" at runtime
    public static final String[] CATEGORY_DYNAMIC = {"xpath://a[@id='itemc' and text()='%s']", "text:%s"};

    // --- PRODUCT PAGE ---
    public static final String[] ADD_TO_CART_BTN = {"xpath://a[text()='Add to cart']", "css:a.btn-success"};
    public static final String[] PRODUCT_TITLE = {"xpath://h2[@class='name']", "css:h2.name"};

    // --- CART PAGE ---
    public static final String[] PLACE_ORDER_BTN = {"xpath://button[text()='Place Order']", "css:button.btn-success"};
    public static final String[] DELETE_PRODUCT = {"xpath://td[text()='%s']/following-sibling::td/a[text()='Delete']"};
}
