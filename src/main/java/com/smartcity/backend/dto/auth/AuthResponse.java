package com.smartcity.backend.dto.auth;

public class AuthResponse {

    private String token;
    private String tokenType;
    private UserSummaryResponse user;

    public AuthResponse() {
    }

    public AuthResponse(String token, String tokenType, UserSummaryResponse user) {
        this.token = token;
        this.tokenType = tokenType;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UserSummaryResponse getUser() {
        return user;
    }

    public void setUser(UserSummaryResponse user) {
        this.user = user;
    }
}
