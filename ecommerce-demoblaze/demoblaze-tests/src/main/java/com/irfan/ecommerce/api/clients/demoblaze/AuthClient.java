package com.irfan.ecommerce.api.clients.demoblaze;

import com.irfan.ecommerce.api.clients.BaseApiClient;
import com.irfan.ecommerce.api.payloads.demoblaze.*;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class AuthClient extends BaseApiClient {

    public AuthClient() {
        super("demoblaze"); // THE FIX: Bridges the child to the parent
    }

    public String getAuthToken(String username, String password) {
        // Restful-Booker uses "username" and "password" in the body
        LoginRequest payload = new LoginRequest(username, password);
        String endpoint = getProperty("api.endpoint.auth");

        logger.info("🔐 AUTH: Fetching token for Booker API...");
        
        Response response = given()
                .spec(getRequestSpec())
                .body(payload)
            .post(endpoint);

        handleApiFailure(response, endpoint);

        // Extracting the clean JSON key "token"
        String token = response.jsonPath().getString("token");
        
        if (token == null) {
            throw new RuntimeException("🛑 AUTH_FAILURE: No token returned. Check credentials.");
        }

        logger.info("✅ AUTH_SUCCESS: Token generated.");
        return token;
    }
}