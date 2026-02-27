package com.irfan.ecommerce.api.clients.restfulbooker;

import com.irfan.ecommerce.api.clients.BaseApiClient;
import com.irfan.ecommerce.api.payloads.restfulbooker.AuthRequest;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;

/**
 * THE WALMART RESUME REF: "Built a Multi-Tenant Auth Manager to handle 
 * diverse authentication protocols across microservices."
 * * THE PROBLEM: Restful-Booker uses a standard JSON 'token' key, while 
 * other legacy systems (like Demoblaze) use custom string prefixes.
 * THE SOLUTION: Created a dedicated AuthClient for the Restful-Booker 
 * domain to keep the parsing logic isolated.
 */
public class AuthClient extends BaseApiClient {

    /**
     * THE WALMART RESUME REF: "Optimized Multi-Service Auth Handlers 
     * by implementing Project-Specific Constructor Routing."
     */
    public AuthClient() {
        super("booker"); // FIX 1: Satisfies the BaseApiClient(String) requirement
    }

    public String getAuthToken(String user, String pass) {
    String endpoint = getProperty("api.endpoint.auth");
    
    // Using the POJO instead of a HashMap
    AuthRequest authPayload = new AuthRequest(user, pass);

    logger.info("🔐 AUTH: Fetching token using AuthRequest POJO...");

    Response response = given()
            .spec(getRequestSpec())
            .body(authPayload) // Jackson converts this object to JSON
        .when()
            .post(endpoint);

    handleApiFailure(response, endpoint);
    return response.jsonPath().getString("token");
    }
}