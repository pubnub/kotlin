package com.pubnub.api.managers.token_manager;

public class TokenManager {
    private volatile String token = null;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
