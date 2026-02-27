package com.irfan.ecommerce.api.tests.demoblaze;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.PactTestRun;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.model.MockProviderConfig;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static org.hamcrest.Matchers.notNullValue;

/**
 * LoginContractTest: Final Persistence Implementation.
 * 2026-02-27: Maintaining Walmart comment - Manual Serialization Logic.
 */
public class LoginContractTest {

    @Test(description = "Verify Login API Contract and Force Write JSON")
    public void testLoginContract() {
        // 1. Build the Agreement
        RequestResponsePact pact = ConsumerPactBuilder
            .consumer("DemoBlaze_UI")
            .hasPactWith("DemoBlaze_API")
            .uponReceiving("a request for user login")
                .path("/login")
                .method("POST")
                .body("{\"username\": \"admin\", \"password\": \"admin\"}")
            .willRespondWith()
                .status(200)
                .headers(java.util.Map.of("Content-Type", "application/json"))
                .body("{\"AuthToken\": \"fake-token-123\"}")
            .toPact();

        MockProviderConfig config = MockProviderConfig.createDefault();

        // 2. Execute the Test and capture the Verification Result
        // We use PactTestRun<Void> to simplify the return type to null
        PactVerificationResult result = runConsumerTest(pact, config, (PactTestRun<Void>) (mockServer, context) -> {
            
            RestAssured.given()
                .contentType("application/json")
                .body("{\"username\": \"admin\", \"password\": \"admin\"}")
            .when()
                .post(mockServer.getUrl() + "/login")
            .then()
                .statusCode(200)
                .body("AuthToken", notNullValue());
            
            return null;
        });

        // 3. Handle the Result and Force Save to Disk
        if (result instanceof PactVerificationResult.Ok) {
            // This is the manual save command for non-JUnit runners
            pact.write("target/pacts", PactSpecVersion.V3);
            System.out.println("✅ SUCCESS: Contract saved to target/pacts/DemoBlaze_UI-DemoBlaze_API.json");
        } else {
            System.err.println("❌ PACT FAILURE: " + result.toString());
            // This ensures TestNG marks the test as FAILED if the contract wasn't met
            throw new RuntimeException("Pact Verification Failed: " + result.toString());
        }
    }
}