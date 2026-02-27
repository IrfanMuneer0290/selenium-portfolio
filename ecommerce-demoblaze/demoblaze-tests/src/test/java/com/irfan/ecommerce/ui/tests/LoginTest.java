package com.irfan.ecommerce.ui.tests;

import com.irfan.ecommerce.ui.base.BaseTest;
import com.irfan.ecommerce.util.JsonDataReader;
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
        String username = input.get("username");
        String password = input.get("password");
        String expected = input.get("expectedStatus");

        // PASS THE FLAG: true only if we expect "success"
        boolean shouldAttemptRegistration = expected.equalsIgnoreCase("success");

        loginPage.performLogin(username, password, shouldAttemptRegistration);

        // Final Validation
        if (shouldAttemptRegistration) {
            Assert.assertTrue(homePage.isUserLoggedIn(username), "❌ FAIL: Expected success login.");
        } else {
            Assert.assertFalse(homePage.isUserLoggedIn(username), "✅ SUCCESS: Negative case rejected as expected.");
        }
    }
}
