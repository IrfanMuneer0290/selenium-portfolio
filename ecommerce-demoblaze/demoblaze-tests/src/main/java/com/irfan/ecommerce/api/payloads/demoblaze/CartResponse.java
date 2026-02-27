package com.irfan.ecommerce.api.payloads;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CartResponse {
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("status")
    private String status;

    public CartResponse() {} // Jackson needs this

    public String getErrorMessage() { return errorMessage; }
    public String getStatus() { return status; }
}
