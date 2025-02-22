package com.example.SpotifyConverter.infra.repositories;

import com.example.SpotifyConverter.entities.SpotifyTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotifyTrackRepository extends JpaRepository<SpotifyTrack, String> {
    Optional<SpotifyTrack> findByIdSpotify(String musicId);
}
