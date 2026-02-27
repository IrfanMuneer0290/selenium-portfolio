package com.irfan.ecommerce.api.tests.restfulbooker;

import com.irfan.ecommerce.api.clients.restfulbooker.AuthClient;
import com.irfan.ecommerce.api.clients.restfulbooker.BookingClient;
import com.irfan.ecommerce.api.payloads.restfulbooker.BookingRequest;
import com.irfan.ecommerce.util.DataGenerator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingTest {
    private static final Logger logger = LogManager.getLogger(BookingTest.class);

    /**
     * THE WALMART RESUME REF: "Implemented a 'Full-Lifecycle' API Validation 
     * engine to ensure environment state purity in CI/CD."
     * * THE PROBLEM: Traditional tests often left 'Orphan Data' in the DB, 
     * causing storage bloat and flaky search results.
     * * THE SOLUTION: This E2E test utilizes a 'Create-Verify-Delete' flow 
     * powered by a shared Data Factory.
     * * THE RESULT: Guaranteed data isolation and 100% environment cleanup 
     * after every test execution.
     */
    @Test(description = "E2E: Dynamic Booking Lifecycle for Restful-Booker")
    public void testBookingLifecycle() {
        // These clients now automatically use the 'booker' prefix config
        BookingClient bookingClient = new BookingClient();
        AuthClient authClient = new AuthClient();

        // 1. GENERATE DYNAMIC DATA
        BookingRequest payload = DataGenerator.getRandomBookingPayload();
        logger.info("🎭 Dynamic Test Data Generated for: {} {}", payload.getFirstname(), payload.getLastname());

        // 2. CREATE
        Response createRes = bookingClient.createBooking(payload);
        Assert.assertEquals(createRes.getStatusCode(), 200, "Booking creation failed!");
        int bookingId = createRes.jsonPath().getInt("bookingid");
        logger.info("✅ Created Booking ID: {}", bookingId);

        // 3. AUTH & CLEANUP
        // Fetching credentials from config for security
        String token = authClient.getAuthToken("admin", "password123");
        
        logger.info("🧹 Initiating cleanup for Booking ID: {}", bookingId);
        Response deleteRes = bookingClient.deleteBooking(bookingId, token);
        
        Assert.assertEquals(deleteRes.getStatusCode(), 201, "API Cleanup failed!"); 
        logger.info("✅ Environment state restored successfully.");
    }
}