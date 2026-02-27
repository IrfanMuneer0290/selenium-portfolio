package com.irfan.ecommerce.api.payloads.restfulbooker;

/**
 * THE WALMART RESUME REF: "Standardized API Authentication payloads using 
 * Type-Safe POJOs to ensure cross-team data consistency."
 * * THE PROBLEM: Different teams were using different keys (e.g., 'user' vs 'username') 
 * leading to 400 Bad Request errors in CI.
 * * THE SOLUTION: This AuthRequest POJO. It acts as the single source of truth 
 * for what the authentication service requires.
 */
public class AuthRequest {
    private String username;
    private String password;

    // Standard Constructors
    public AuthRequest() {}

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}