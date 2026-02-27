package com.irfan.ecommerce.api.clients.demoblaze;

import com.irfan.ecommerce.api.clients.BaseApiClient;
import com.irfan.ecommerce.api.payloads.demoblaze.*;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

/**
 * CartClient: Optimized for high-speed API data injection.
 * 2026-02-27: Hardened against null-path exceptions via explicit property validation.
 */
public class CartClient extends BaseApiClient {

    public CartClient() {
        super("demoblaze"); // THE FIX: Bridges the child to the parent
    }

    public void addToCart(String productId, String token) {
        // 1. Fetch endpoint from config.properties (mapped via prefix in BaseApiClient)
        String endpoint = getProperty("api.endpoint.addtocart");

        // 2. DEFENSIVE GUARD: Avoid the "IllegalArgumentException: path cannot be null"
        if (endpoint == null) {
            logger.error("🛑 CONFIG ERROR: 'demoblaze.api.endpoint.addtocart' is missing!");
            throw new RuntimeException("API Endpoint not configured for Cart Injection.");
        }

        // 3. PAYLOAD CONSTRUCTION
        AddToCartRequest payload = new AddToCartRequest(productId, token, true);

        logger.info("🛒 API_INJECTION: Adding Product ID [{}] to cart...", productId);

        // 4. EXECUTION
        Response response = given()
                .spec(getRequestSpec())
                .body(payload)
                .when()
                .post(endpoint);

        // 5. VALIDATION: Using the BaseApiClient handler
        handleApiFailure(response, endpoint);
        
        logger.info("✅ API_SUCCESS: Product [{}] successfully injected into session.", productId);
    }
}