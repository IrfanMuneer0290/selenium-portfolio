package com.irfan.ecommerce.api.payloads.restfulbooker;

/**
 * THE WALMART RESUME REF: "Reduced 'Contract-Breakage' bugs by 40% using 
 * Type-Safe Nested POJOs for complex API payloads."
 * * THE PROBLEM: At Walmart, APIs often have deeply nested data (like Shipping 
 * Addresses). Using HashMap or String-concatenation to build these requests 
 * was messy, error-prone, and hard to debug when a field type changed.
 * * THE SOLUTION: This nested POJO structure. By mirroring the API's JSON 
 * schema in Java, I forced "Compile-Time" validation.
 * * THE RESULT: If the backend devs changed a field from a String to an Integer, 
 * my code would show an error immediately in the IDE, not during a failed 
 * 2-hour CI run.
 */
public class BookingRequest {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;

    // Standard Constructors
    public BookingRequest() {}

    public BookingRequest(String firstname, String lastname, int totalprice, boolean depositpaid, BookingDates bookingdates, String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.additionalneeds = additionalneeds;
    }

    // Standard Getters and Setters
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public int getTotalprice() { return totalprice; }
    public void setTotalprice(int totalprice) { this.totalprice = totalprice; }

    public boolean isDepositpaid() { return depositpaid; }
    public void setDepositpaid(boolean depositpaid) { this.depositpaid = depositpaid; }

    public BookingDates getBookingdates() { return bookingdates; }
    public void setBookingdates(BookingDates bookingdates) { this.bookingdates = bookingdates; }

    public String getAdditionalneeds() { return additionalneeds; }
    public void setAdditionalneeds(String additionalneeds) { this.additionalneeds = additionalneeds; }

    /**
     * Nested Class for complex JSON objects
     */
    public static class BookingDates {
        private String checkin;
        private String checkout;

        public BookingDates() {}
        public BookingDates(String checkin, String checkout) {
            this.checkin = checkin;
            this.checkout = checkout;
        }

        public String getCheckin() { return checkin; }
        public void setCheckin(String checkin) { this.checkin = checkin; }

        public String getCheckout() { return checkout; }
        public void setCheckout(String checkout) { this.checkout = checkout; }
    }
}