package com.example.SpotifyConverter.infra.controllers;

import com.example.SpotifyConverter.application.useCase.CreateYoutubeLink;
import com.example.SpotifyConverter.entities.SpotifyTrack;
import com.example.SpotifyConverter.application.useCase.GetSpotifyToken;
import com.example.SpotifyConverter.infra.controllers.dto.SpotifyControllerDto;
import com.example.SpotifyConverter.infra.controllers.dto.SpotifyTrackDto;
import com.example.SpotifyConverter.infra.services.SpotifyServices;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping
public class SpotifyController {
    private final CreateYoutubeLink createYoutubeLink;
    private final SpotifyServices spotifyServices;
    public SpotifyController(CreateYoutubeLink createYoutubeLink, SpotifyServices spotifyServices) {
        this.createYoutubeLink = createYoutubeLink;
        this.spotifyServices = spotifyServices;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("convert/youtube")
    public ResponseEntity<?> convertToYoutubeToken(@RequestBody SpotifyControllerDto dto) throws Exception {
        Pattern pattern = Pattern.compile("(track/)(.+)");
        Matcher matcher = pattern.matcher(dto.link());
        if (!matcher.find()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Link inválido. Certifique-se de que o link contém '/track/{id}'");

        }
        String musicId = matcher.group(2);
        SpotifyTrack track = spotifyServices.fetchTrack(musicId);
        var trackWithLink = createYoutubeLink.execute(musicId, dto.siteToConvert(), track.getMusicName(), track.getArtistsName(), track.getAlbumsName());
        if (trackWithLink == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Não foi encontrado um link correspondente com a música enviada");
        }
        var trackDto = new SpotifyTrackDto(trackWithLink.getMusicName(), trackWithLink.getArtistsName(), trackWithLink.getAlbumsName(), trackWithLink.getYoutubeLink());
        return new ResponseEntity<>(trackDto, HttpStatus.OK);

    }
}