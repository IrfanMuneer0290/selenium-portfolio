package com.irfan.ecommerce.api.clients.demoblaze;

import com.irfan.ecommerce.api.clients.BaseApiClient;
import com.irfan.ecommerce.api.payloads.demoblaze.*;
import static io.restassured.RestAssured.given;

public class CartClient extends BaseApiClient {

    public CartClient() {
        super("demoblaze"); // Explicitly routing Demoblaze traffic
    }

    public void addToCart(String productId, String token) {

        // 🛠️ THE FIX: Use standard Constructor
        AddToCartRequest payload = new AddToCartRequest(productId, token, true);

        given()
                .spec(getRequestSpec())
                .body(payload)
                .when()
                .post(getProperty("api.endpoint.addtocart"))
                .then()
                .spec(responseSpec);
    }
}
