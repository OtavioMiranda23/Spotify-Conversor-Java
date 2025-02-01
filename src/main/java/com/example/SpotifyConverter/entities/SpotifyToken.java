package com.example.SpotifyConverter.entities;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SpotifyToken {
    String accessToken;
    String tokenType;
    int expiresIn;

    @JsonCreator
    public SpotifyToken(
            @JsonProperty("access_token") String access_token,
            @JsonProperty("token_type") String token_type,
            @JsonProperty("expires_in") int expires_in
    ) {
        this.accessToken = access_token;
        this.tokenType = token_type;
        this.expiresIn = expires_in;
    }
    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

}
