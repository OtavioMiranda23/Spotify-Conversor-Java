package com.example.SpotifyConverter.infra.controllers;

import com.example.SpotifyConverter.entities.SpotifyToken;
import com.example.SpotifyConverter.entities.SpotifyTrack;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Map;

@RestController
@RequestMapping
public class Authenticate {
    @Value("${app.clientId}")
    String clientId;

    @Value("${app.clientSecret}")
    String clientSecret;

    String token;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("auth")
    public String getAuth() throws JsonProcessingException {
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
        this.token = tokenResponse.getAccessToken();
        return this.token;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("convert/youtube")
    public Map<String, Object> convertToYoutubeToken(@RequestBody String musicId) throws Exception {
        String url = "https://api.spotify.com/v1/tracks/" + musicId;
        HttpClient client = HttpClient.newHttpClient();
        String token = "Bearer " + this.token;
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).headers("Authorization", token).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());
        JsonNode artistsNode = rootNode.path("artists");
        LinkedList<String> artistsFounded = new LinkedList<>();
        for (JsonNode artistNode: artistsNode) {
            String name = artistNode.path("name").asText();
            if (!name.isEmpty()) {
                artistsFounded.add(name);
            }
        }
        String albumName = rootNode.path("album").path("name").asText();
        SpotifyTrack spotifyTrack = new SpotifyTrack(musicId, rootNode.path("name").asText(), artistsFounded, albumName);
        return spotifyTrack.toMap();
    }
}