package com.irfan.ecommerce.ui.tests;

import com.irfan.ecommerce.ui.base.BaseTest;
import com.irfan.ecommerce.util.JsonDataReader;
import com.irfan.ecommerce.util.GenericActions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;

public class LoginTest extends BaseTest {

    @DataProvider(name = "getLoginData")
    public Object[][] getData() {
        // WALMART IMPACT: Decoupling data from code for CI/CD portability
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/testdata/loginData.json";
        List<Map<String, String>> dataMap = JsonDataReader.getTestData(dataPath);

        Object[][] data = new Object[dataMap.size()][1];
        for (int i = 0; i < dataMap.size(); i++) {
            data[i][0] = dataMap.get(i);
        }
        return data;
    }

    @Test(dataProvider = "getLoginData")
    public void verifyLoginScenarios(Map<String, String> input) {
        String scenario = input.get("scenario");
        String expected = input.get("expectedStatus");

        reportLog("🚀 STARTING SCENARIO: " + scenario);
        loginPage.performLogin(input.get("username"), input.get("password"));

        // 🛡️ THE WALMART FIX: Handle the Alert FIRST to avoid UnhandledAlertException
        String alertMessage = GenericActions.getAlertTextAndAccept();

        if (expected.equalsIgnoreCase("success")) {
            // If we expect success but got an alert, it's a functional failure (e.g., user
            // deleted from sandbox)
            if (!alertMessage.equals("NO_ALERT_PRESENT")) {
                Assert.fail("❌ LOGIN FAILED: Expected success but got alert: " + alertMessage);
            }

            boolean isUserLoggedIn = homePage.isUserLoggedIn(input.get("username"));
            Assert.assertTrue(isUserLoggedIn, "❌ FAIL: Navbar did not display username.");
        } else {
            // Validation for expected failure scenarios
            Assert.assertNotEquals(alertMessage, "NO_ALERT_PRESENT", "❌ FAIL: Expected error alert but none appeared.");
            reportLog("✅ SUCCESS: System correctly rejected login with alert: " + alertMessage);
        }
    }

}
