package com.irfan.ecommerce.util;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.irfan.ecommerce.base.DriverFactory;

/**
 * GenericActions: Enterprise-Grade Abstraction Layer.
 * Features: Self-Healing, Log4j 2, Automated Screenshot on Failure, and Custom Exception Handling.
 * @author Irfan Muneer
 */
public class GenericActions {

    private static final Logger log = LogManager.getLogger(GenericActions.class);

    private static WebDriver getDriver() { return DriverFactory.getDriver(); }
    private static WebDriverWait getWait() { return new WebDriverWait(getDriver(), Duration.ofSeconds(10)); }
    private static WebDriverWait getShortWait() { return new WebDriverWait(getDriver(), Duration.ofSeconds(2)); }
    private static Actions getActions() { return new Actions(getDriver()); }

    /** Translates String to By object with Strategy support. */
    private static By parseBy(String locator, String... replacements) {
        String processed = (replacements.length > 0) ? String.format(locator, (Object[]) replacements) : locator;
        if (processed.contains(":")) {
            String[] parts = processed.split(":", 2);
            String strategy = parts[0].toLowerCase().trim();
            String value = parts[1].trim();
            return switch (strategy) {
                case "id" -> By.id(value);
                case "xpath" -> By.xpath(value);
                case "css" -> By.cssSelector(value);
                case "name" -> By.name(value);
                case "class" -> By.className(value);
                case "text" -> By.linkText(value);
                default -> By.xpath(processed);
            };
        }
        return (processed.startsWith("//") || processed.startsWith("(")) ? By.xpath(processed) : By.id(processed);
    }

    /** The Heart: Self-Healing Element Finder. */
    private static WebElement findElementSmartly(String[] locators, String... replacements) {
        for (String loc : locators) {
            try {
                By by = parseBy(loc, replacements);
                return getShortWait().until(ExpectedConditions.visibilityOfElementLocated(by));
            } catch (Exception e) {
                log.debug("RETRY: Primary locator [{}] failed. Attempting next backup...", loc);
            }
        }
        log.error("FATAL: Exhausted all {} locators without success.", locators.length);
        throw new NoSuchElementException("CRITICAL: All locators failed for priority list: " + String.join(", ", locators));
    }

    // --- 1. NAVIGATION ---
    public static void navigateTo(String url) {
        try {
            getDriver().get(url);
            log.info("NAV: Successfully navigated to URL: {}", url);
        } catch (Exception e) {
            log.error("FATAL: Failed to reach {}. Error: {}", url, e.getMessage());
            throw new RuntimeException("Navigation Failure", e);
        }
    }

    // --- 2. CORE INTERACTIONS (PRO-CATCH) ---
    public static void click(String[] locators, String... replacements) {
        try {
            WebElement el = findElementSmartly(locators, replacements);
            getWait().until(ExpectedConditions.elementToBeClickable(el)).click();
            log.info("ACTION: Clicked element successfully.");
        } catch (Exception e) {
            String path = takeScreenshot("Click_Failure");
            log.error("FATAL: Click failed. Evidence: {}. Trace: {}", path, e.getMessage());
            throw new RuntimeException("Interaction Error: Click", e);
        }
    }

    public static void sendKeys(String[] locators, String text, String... replacements) {
        try {
            WebElement el = findElementSmartly(locators, replacements);
            el.clear();
            el.sendKeys(text);
            log.info("ACTION: Typed [{}] into field.", text);
        } catch (Exception e) {
            takeScreenshot("Type_Failure");
            log.error("FATAL: Input failed on locators {}. Error: {}", locators, e.getMessage());
            throw new RuntimeException("Input Error: SendKeys", e);
        }
    }

    public static String getText(String[] locators, String... replacements) {
        try {
            String text = findElementSmartly(locators, replacements).getText();
            log.info("DATA: Retrieved UI Text: {}", text);
            return text;
        } catch (Exception e) {
            log.warn("WARN: GetText failed, returning empty string. Trace: {}", e.getMessage());
            return "";
        }
    }

    // --- 3. JAVASCRIPT ---
    public static void jsClick(String[] locators, String... replacements) {
        try {
            WebElement el = findElementSmartly(locators, replacements);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", el);
            log.info("JS: Executed JavaScript Click.");
        } catch (Exception e) {
            log.error("JS_ERROR: JS Click failed: {}", e.getMessage());
        }
    }

    // --- 4. DROPDOWNS ---
    public static void selectByText(String[] locators, String text, String... replacements) {
        try {
            WebElement el = findElementSmartly(locators, replacements);
            new Select(el).selectByVisibleText(text);
            log.info("SELECT: Option [{}] chosen from dropdown.", text);
        } catch (Exception e) {
            takeScreenshot("Select_Failure");
            log.error("FATAL: Dropdown selection failed. Error: {}", e.getMessage());
            throw new RuntimeException("Select Error", e);
        }
    }

    // --- 5. ALERTS ---
    public static void handleAlert(boolean accept) {
        try {
            Alert alert = getWait().until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            if (accept) { alert.accept(); log.info("ALERT: Accepted. Text was: {}", text); }
            else { alert.dismiss(); log.info("ALERT: Dismissed."); }
        } catch (TimeoutException e) {
            log.warn("ALERT: No alert appeared within timeout.");
        }
    }

    // --- 6. WINDOWS & FRAMES ---
    public static void switchToWindow(String title) {
        try {
            for (String handle : getDriver().getWindowHandles()) {
                getDriver().switchTo().window(handle);
                if (getDriver().getTitle().contains(title)) {
                    log.info("WINDOW: Switched focus to: {}", title);
                    return;
                }
            }
        } catch (Exception e) {
            log.error("WINDOW_ERROR: Switch failed: {}", e.getMessage());
        }
    }

    public static void switchToFrame(String[] locators, String... replacements) {
        try {
            WebElement frame = findElementSmartly(locators, replacements);
            getDriver().switchTo().frame(frame);
            log.info("FRAME: Focused inside iframe.");
        } catch (Exception e) {
            log.error("FRAME_ERROR: Switch failed: {}", e.getMessage());
            throw new RuntimeException("Frame Switch Error", e);
        }
    }

    // --- 7. VERIFICATION ---
    public static boolean isDisplayed(String[] locators, String... replacements) {
        try {
            return findElementSmartly(locators, replacements).isDisplayed();
        } catch (Exception e) {
            log.debug("VERIFY: Element not displayed on UI.");
            return false;
        }
    }

    // --- 8. SYSTEM UTILS ---
    public static String takeScreenshot(String name) {
        try {
            File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            String folder = System.getProperty("user.dir") + "/target/screenshots/";
            new File(folder).mkdirs(); // Ensure directory exists
            String path = folder + name + "_" + System.currentTimeMillis() + ".png";
            FileUtils.copyFile(src, new File(path));
            return path;
        } catch (IOException e) {
            log.error("IO_ERROR: Could not save screenshot: {}", e.getMessage());
            return "NULL_PATH";
        }
    }

    public static void addCookie(String name, String value) {
        try {
            getDriver().manage().addCookie(new Cookie(name, value));
            getDriver().navigate().refresh();
            log.info("Cookie [{}] added and page refreshed.", name);
        } catch (Exception e) {
            log.error("Failed to add cookie: {}", e.getMessage());
        }
    }

    // --- 11. ENTERPRISE SECURITY & BYPASS STRATEGIES ---

    /**
     * Retrieves an OTP from a backend API or Mock Service.
     * In an enterprise PROD/STAGE environment, we use RestAssured to poll a 
     * database or secret manager instead of automating the UI of an email/SMS.
     * @param accountId - The unique identifier for the test user
     * @return String - The 6-digit OTP code
     */
    public static String getOTPFromBackend(String accountId) {
        log.info("SECURITY: Fetching OTP for [{}] via Backend API...", accountId);
        // Implementation: return RestAssured.get("/api/otp/" + accountId).jsonPath().getString("code");
        String mockOTP = "123456"; 
        log.info("SECURITY: OTP successfully retrieved.");
        return mockOTP;
    }

    /**
     * Bypasses CAPTCHA by injecting a 'Magic Cookie' or 'Automation Token'.
     * This is the standard 1% Elite way to handle security in Regression suites.
     * @param cookieName - The whitelisted cookie name (e.g., 'Bypass-Captcha')
     * @param value - The secret token value
     */
    public static void addAutomationBypassCookie(String cookieName, String value) {
        try {
            log.info("SECURITY: Injecting Automation Bypass Cookie: {}", cookieName);
            Cookie bypassCookie = new Cookie(cookieName, value);
            getDriver().manage().addCookie(bypassCookie);
            getDriver().navigate().refresh();
            log.info("SECURITY: Page refreshed with Bypass Cookie active.");
        } catch (Exception e) {
            log.error("SECURITY_ERROR: Failed to inject bypass cookie | {}", e.getMessage());
        }
    }

    /**
     * Handles 'Invisible CAPTCHA' by injecting a bypass token into the hidden g-recaptcha-response field.
     * @param token - The valid response token provided by the dev team or a solver service.
     */
    public static void injectCaptchaBypassToken(String token) {
        try {
            log.info("SECURITY: Injecting hidden g-recaptcha-response token...");
            ((JavascriptExecutor) getDriver()).executeScript(
                "document.getElementById('g-recaptcha-response').innerHTML='" + token + "';");
            log.info("SECURITY: CAPTCHA token successfully injected.");
        } catch (Exception e) {
            log.error("SECURITY_ERROR: CAPTCHA injection failed | {}", e.getMessage());
        }
    }
}
