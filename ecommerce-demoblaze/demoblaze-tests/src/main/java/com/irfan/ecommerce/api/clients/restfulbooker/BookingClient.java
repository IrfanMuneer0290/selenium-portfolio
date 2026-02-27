package com.irfan.ecommerce.api.clients.restfulbooker;

import com.irfan.ecommerce.api.clients.BaseApiClient;
import com.irfan.ecommerce.api.payloads.restfulbooker.BookingRequest;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

/**
 * THE WALMART RESUME REF: "Built a Domain-Isolated Client Layer to handle 
 * service-specific CRUD operations within a Shared Framework."
 * * THE PROBLEM: Hardcoding endpoints across 100+ tests made framework updates 
 * impossible when the Backend team changed API routes.
 * * THE SOLUTION: Centralized endpoint management via this BookingClient. 
 * Test scripts interact with methods, not URLs.
 * * THE RESULT: 100% isolation of Test Logic from Network Logic, 
 * making the framework 10x easier to maintain."
 */
public class BookingClient extends BaseApiClient {

    public BookingClient() {
        // This 'Handshake' tells the BaseApiClient to load 'booker.' properties
        super("booker");
    }

    public Response createBooking(BookingRequest payload) {
        String endpoint = getProperty("api.endpoint.booking");
        
        logger.info("📡 API_REQUEST [POST]: Creating new booking at {}", endpoint);
        logPayload(payload, "New Booking Request");

        Response response = given()
                .spec(getRequestSpec())
                .body(payload)
            .when()
                .post(endpoint);

        handleApiFailure(response, endpoint);
        return response;
    }

    public Response deleteBooking(int bookingId, String token) {
        String endpoint = getProperty("api.endpoint.booking") + "/" + bookingId;
        
        logger.warn("🗑️ API_REQUEST [DELETE]: Removing ID: {}", bookingId);

        Response response = given()
                .spec(getRequestSpec())
                .header("Cookie", "token=" + token) // Booker requires Token in a Cookie
            .when()
                .delete(endpoint);

        handleApiFailure(response, endpoint);
        return response;
    }
}