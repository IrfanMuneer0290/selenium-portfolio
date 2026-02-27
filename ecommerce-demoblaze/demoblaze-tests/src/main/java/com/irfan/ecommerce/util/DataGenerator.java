package com.irfan.ecommerce.util;

import com.github.javafaker.Faker;
import com.irfan.ecommerce.api.payloads.restfulbooker.BookingRequest;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * THE WALMART RESUME REF: "Engineered a Unified Data Factory to synchronize 
 * State between UI and API test layers."
 * * THE PROBLEM: When testing E2E, the UI team used "John" and the API team 
 * used "Doe." This made it impossible to trace a single transaction across 
 * different testing tiers.
 * * THE SOLUTION: A centralized DataGenerator. It serves as the 'Single Source 
 * of Truth' for both Selenium and RestAssured.
 * * THE RESULT: 100% data consistency. I can generate a user via API and 
 * immediately log in with that same dynamic user in the UI.
 */
public class DataGenerator {
    private static final Faker faker = new Faker();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // --- GENERIC UI DATA ---
    public static String getFirstName() { return faker.name().firstName(); }
    public static String getLastName() { return faker.name().lastName(); }
    public static String getFullAddress() { return faker.address().fullAddress(); }
    public static String getEmail() { return faker.internet().emailAddress(); }

    // --- API SPECIFIC DATA MODELS ---
    public static BookingRequest getRandomBookingPayload() {
        BookingRequest.BookingDates dates = new BookingRequest.BookingDates(
            sdf.format(faker.date().future(5, TimeUnit.DAYS)),
            sdf.format(faker.date().future(10, TimeUnit.DAYS))
        );

        return new BookingRequest(
            getFirstName(),
            getLastName(),
            faker.number().numberBetween(100, 500),
            faker.bool().bool(),
            dates,
            faker.food().dish()
        );
    }
}