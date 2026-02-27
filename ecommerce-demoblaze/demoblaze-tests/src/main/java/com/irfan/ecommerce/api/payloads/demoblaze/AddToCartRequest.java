package com.irfan.ecommerce.api.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddToCartRequest {
    @JsonProperty("id") private String id;
    @JsonProperty("cookie") private String cookie;
    @JsonProperty("flag") private boolean flag;

    public AddToCartRequest() {}

    public AddToCartRequest(String id, String cookie, boolean flag) {
        this.id = id;
        this.cookie = cookie;
        this.flag = flag;
    }
    // Standard Getters
    public String getId() { return id; }
    public String getCookie() { return cookie; }
    public boolean isFlag() { return flag; }
}
