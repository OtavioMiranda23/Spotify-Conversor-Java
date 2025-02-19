package com.example.SpotifyConverter.application.useCase;

import com.example.SpotifyConverter.entities.SpotifyTrack;
import com.example.SpotifyConverter.infra.repositories.SpotifyTrackRepository;
import org.springframework.http.ResponseEntity;

public class GetLinkById {
    private final SpotifyTrackRepository repository;

    public GetLinkById(SpotifyTrackRepository repository) {
        this.repository = repository;
    }

    public SpotifyTrack execute(String id) {
        var track = repository.findAll();
        System.out.println("->" + track);
        return repository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build()).getBody();
    }
}