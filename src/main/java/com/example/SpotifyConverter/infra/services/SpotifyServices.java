package com.example.SpotifyConverter.infra.services;

import com.example.SpotifyConverter.application.useCase.GetSpotifyToken;
import com.example.SpotifyConverter.entities.SpotifyTrack;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@Service
public class SpotifyServices {
    private final GetSpotifyToken getSpotifyToken;
    private String token;
    private final ObjectMapper mapper;

    public SpotifyServices(GetSpotifyToken getSpotifyToken, ObjectMapper objectMapper){
        this.getSpotifyToken = getSpotifyToken;
        this.mapper = objectMapper;
    }

    public SpotifyTrack fetchTrack(String musicId) throws Exception {
        this.token = getSpotifyToken.execute();
        String url = "https://api.spotify.com/v1/tracks/" + musicId;
        HttpClient client = HttpClient.newHttpClient();
        String token = "Bearer " + this.token;
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .headers("Authorization", token)
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode rootNode = mapper.readTree(response.body());
        return this.extractTrack(rootNode, musicId);
    }

    private SpotifyTrack extractTrack(JsonNode rootNode, String musicId) throws Exception {
        var artistsFounded = new ArrayList<String>();
        JsonNode artistsNode = rootNode.path("artists");
        for (JsonNode artistNode: artistsNode) {
            String name = artistNode.path("name").asText();
            if (!name.isEmpty()) {
                artistsFounded.add(name);
            }
        }
        String albumName = rootNode.path("album").path("name").asText();
        var musicName = rootNode.path("name").asText();
        return new SpotifyTrack(musicId, musicName, artistsFounded, albumName);
    }
}
