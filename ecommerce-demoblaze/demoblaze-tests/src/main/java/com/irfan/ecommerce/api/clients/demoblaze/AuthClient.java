package com.irfan.ecommerce.api.clients.demoblaze;

import com.irfan.ecommerce.api.clients.BaseApiClient;
import com.irfan.ecommerce.api.payloads.demoblaze.*;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

/**
 * AuthClient: Dedicated Authentication for DemoBlaze.
 * 2026-02-27: Fixed property key alignment to prevent null-path errors.
 */
public class AuthClient extends BaseApiClient {

    public AuthClient() {
        // Bridges to the "demoblaze" prefix in config.properties
        super("demoblaze");
    }

   public String getAuthToken(String username, String password) {
        LoginRequest payload = new LoginRequest(username, password);
        String loginEndpoint = getProperty("api.endpoint.login");

        logger.info("🔐 AUTH: Attempting login for: {}", username);

        Response response = given()
                .spec(getRequestSpec())
                .body(payload)
                .post(loginEndpoint);

        // DEMOBLAZE TRICK: It often returns "Auth_token: ..." as a raw string 
        // or inside the body. Let's try to find it dynamically.
        String body = response.asString();
        String token = "";

        if (body.contains("Auth_token")) {
             token = body.replace("Auth_token: ", "").trim();
        }

        // 1. SELF-HEALING: If no token found, register
        if (token.isEmpty() || body.contains("errorMessage")) {
            logger.warn("⚠️ AUTH_FAILED: Attempting auto-registration for: {}", username);

            given()
                    .spec(getRequestSpec())
                    .body(payload)
                    .post("/signup"); // Standard DemoBlaze signup

            // 2. RETRY
            response = given()
                    .spec(getRequestSpec())
                    .body(payload)
                    .post(loginEndpoint);
            
            body = response.asString();
            if (body.contains("Auth_token")) {
                token = body.replace("Auth_token: ", "").trim();
            }
        }

        // 3. FINAL VALIDATION
        if (token.isEmpty()) {
            logger.error("🛑 CRITICAL: API Response Body: {}", body);
            throw new RuntimeException("🛑 AUTH_FAILURE: Response did not contain Auth_token.");
        }

        logger.info("✅ AUTH_SUCCESS: Token acquired.");
        return token;
    }
}