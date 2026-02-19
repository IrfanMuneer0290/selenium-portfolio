package com.irfan.ecommerce.ui.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.irfan.ecommerce.util.ConfigReader;
import com.irfan.ecommerce.util.GenericActions;
import com.irfan.ecommerce.util.PropertyReader;

/**
 * BaseTest: The "Orchestrator" for all test classes.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: When I first started at Walmart, everyone was creating 
 *   WebDriver instances inside their test classes. It was a disaster—if 
 *   we wanted to switch to Docker or Headless mode, we had to change 
 *   100 different files. Also, browsers were leaking memory and killing the CI.
 * - WHAT I DID: I centralized everything here. Now, the tests just ask 
 *   the 'DriverFactory' for a thread-safe session. This class handles 
 *   the lifecycle—starting fresh and cleaning up perfectly.
 * - THE RESULT: Switching the whole company from Local to Remote/Docker 
 *   execution now takes only 1 second by changing a single parameter.
 * 
 * Author: Irfan Muneer
 */

public class BaseTest {
    public WebDriver driver;
    
    @BeforeMethod
    public void setup() {
        driver = DriverFactory.initDriver("chrome");

         // Architect Move: Start the sniffer before navigating
    String browser = ConfigReader.getProperty("browser");
    if ("chrome".equalsIgnoreCase(browser)) {
       // GenericActions.startNetworkSniffer();
    }
    
      // Pulls the URL from qa.properties or prod.properties based on your Maven command
    String baseUrl = PropertyReader.getProperty("url"); 
    driver.get(baseUrl);
    }
    
    @AfterMethod
    public void teardown() {
        if (driver != null) driver.quit();
    }
}
