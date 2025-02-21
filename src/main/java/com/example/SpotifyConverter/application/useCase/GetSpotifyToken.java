package com.example.SpotifyConverter.application.useCase;

import com.example.SpotifyConverter.entities.SpotifyToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class GetSpotifyToken {
    @Value("${app.clientId}")
    String clientId;

    @Value("${app.clientSecret}")
    String clientSecret;

    public String execute() throws JsonProcessingException {
        String idSecret = this.clientId + ":" + this.clientSecret;
        byte[] bytes = idSecret.getBytes();
        byte[] encoded = Base64.getEncoder().encode(bytes);
        String uri = "https://accounts.spotify.com/api/token";
        RestClient restClient = RestClient.create();
        var headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + new String(encoded));
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        String jsonResponse = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        SpotifyToken tokenResponse = mapper.readValue(jsonResponse, SpotifyToken.class);
        return tokenResponse.getAccessToken();
    }
}
