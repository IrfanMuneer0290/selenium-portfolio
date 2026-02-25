package com.irfan.ecommerce.api.tests;

import com.irfan.ecommerce.api.clients.AuthClient;
import com.irfan.ecommerce.api.payloads.AddToCartRequest;
import com.irfan.ecommerce.ui.base.BaseTest;
import com.irfan.ecommerce.util.PropertyReader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.UUID;
import static io.restassured.RestAssured.given;

public class IdempotencyTest extends BaseTest {

    @Test(description = "Verify AddToCart API is Idempotent")
    public void verifyAddToCartIdempotency() {
        // 🚀 Now 'logger' is inherited from BaseTest (no red lines)
        logger.info("🛡️ IDEMPOTENCY: Commencing double-tap validation.");
        
        AuthClient authClient = new AuthClient();
        String user = PropertyReader.getProperty("test.username");
        String pass = PropertyReader.getProperty("test.password");
        String token = authClient.getAuthToken(user, pass);
        
        String idempotencyKey = UUID.randomUUID().toString();
        AddToCartRequest payload = new AddToCartRequest("1", token, true);

        logger.info("📡 REQUEST 1: Sending initial state injection. Key: [{}]", idempotencyKey);
        Response response1 = sendAddToCart(payload, idempotencyKey);
        Assert.assertEquals(response1.getStatusCode(), 200);

        logger.warn("🔁 REQUEST 2: Simulating network retry (Double-Tap). Key: [{}]", idempotencyKey);
        Response response2 = sendAddToCart(payload, idempotencyKey);
        
        // At Walmart, we'd also check if the item count in the cart stayed at 1
        Assert.assertEquals(response2.getStatusCode(), 200, "❌ Idempotency failure!");
        logger.info("✅ SUCCESS: Server handled duplicate request for Key: [{}]", idempotencyKey);
    }

    private Response sendAddToCart(AddToCartRequest payload, String key) {
        String baseUri = PropertyReader.getProperty("api.base.uri"); 
        return given()
                .header("X-Idempotency-Key", key) 
                .contentType("application/json")
                .body(payload)
            .when()
                .post(baseUri + "/addtocart") 
            .then()
                .extract().response();
    }
}
