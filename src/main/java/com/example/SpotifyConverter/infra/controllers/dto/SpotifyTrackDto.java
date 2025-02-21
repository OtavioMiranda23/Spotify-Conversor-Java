package com.example.SpotifyConverter.infra.controllers.dto;

import java.util.List;

public record SpotifyTrackDto(String musicName, List<String> artistsName, String albumsName, String link) {
}
