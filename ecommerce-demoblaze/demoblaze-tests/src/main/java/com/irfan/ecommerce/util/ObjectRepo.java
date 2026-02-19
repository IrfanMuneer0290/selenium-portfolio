package com.irfan.ecommerce.util;

/**
 * ObjectRepo: The "Smart Address Book" of the framework.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: At Walmart, we had hundreds of Page Objects. If a developer 
 *   changed the 'Login' button ID, I had to search through 50 different files 
 *   to find where that button was used. It was a maintenance nightmare.
 * - WHAT I DID: I centralized every single locator here. I also used 
 *   "Priority Arrays." Instead of one ID, I give the framework a list. 
 *   If the ID fails, it automatically tries the XPath or CSS from this list.
 * - THE RESULT: Maintenance is now 10x faster. If a button changes, I fix 
 *   it in ONE line here, and the whole framework is healed instantly.
 * 
 * Author: Irfan Muneer
 */
public class ObjectRepo {

    /**
     * [ARCHITECT NOTE]: Private constructor to hide the implicit public one.
     * This prevents unnecessary object instantiation in heap memory, keeping
     * the JVM lean during massive parallel runs.
     */
    private ObjectRepo() {
        throw new IllegalStateException("Utility class - instantiation is not allowed.");
    }

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
    public static final String[] CATEGORY_DYNAMIC = {"xpath://a[text()='%s']"};

    // --- PRODUCT PAGE ---
    public static final String[] ADD_TO_CART_BTN = {"xpath://a[text()='Add to cart']", "css:a.btn-success"};
    public static final String[] PRODUCT_TITLE = {"xpath://h2[@class='name']", "css:h2.name"};
    public static final String[] PRODUCT_PRICE = {"class:price-container", "xpath://h3[@class='price-container']"};
    public static final String[] PRODUCT_DESC  = {"id:more-information", "xpath://div[@id='more-information']"};


    // --- CART PAGE ---
    public static final String[] PLACE_ORDER_BTN = {"xpath://button[text()='Place Order']", "css:button.btn-success"};
    public static final String[] CART_ITEMS_TABLE = {"xpath://table[@class='table table-bordered table-hover table-striped']"};
    public static final String[] CART_TOTAL_PRICE = {"id:totalp", "xpath://h3[@id='totalp']"};
    public static final String[] CART_PRODUCT_NAME = {"xpath://td[text()='%s']"};
    /**
     * Finds the delete link specifically for the product name provided.
     */
    public static final String[] DELETE_PRODUCT = {"xpath://td[text()='%s']/following-sibling::td/a[text()='Delete']"};
}
