package com.example.SpotifyConverter.infra.controllers;

import com.example.SpotifyConverter.application.useCase.CreateYoutubeLink;
import com.example.SpotifyConverter.entities.SpotifyTrack;
import com.example.SpotifyConverter.application.useCase.GetSpotifyToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@RestController
@RequestMapping
public class SpotifyController {
    String token;

    private final CreateYoutubeLink createYoutubeLink;
    private final GetSpotifyToken getSpotifyToken;
    public SpotifyController(CreateYoutubeLink createYoutubeLink, GetSpotifyToken getSpotifyToken) throws JsonProcessingException {
        this.createYoutubeLink = createYoutubeLink;
        this.getSpotifyToken = getSpotifyToken;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("convert/youtube")
    public ResponseEntity<SpotifyTrack> convertToYoutubeToken(@RequestBody String musicId) throws Exception {
        this.token = getSpotifyToken.execute();
        String url = "https://api.spotify.com/v1/tracks/" + musicId;
        HttpClient client = HttpClient.newHttpClient();
        String token = "Bearer " + this.token;
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).headers("Authorization", token).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());
        JsonNode artistsNode = rootNode.path("artists");
        var artistsFounded = new ArrayList<String>();
        for (JsonNode artistNode: artistsNode) {
            String name = artistNode.path("name").asText();
            if (!name.isEmpty()) {
                artistsFounded.add(name);
            }
        }
        String albumName = rootNode.path("album").path("name").asText();
        var musicName = rootNode.path("name").asText();
        var track = createYoutubeLink.execute(musicId, musicName, artistsFounded, albumName);
        return new ResponseEntity<>(track, HttpStatus.OK);
    }
}