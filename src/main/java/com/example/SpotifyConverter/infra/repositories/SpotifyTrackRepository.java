package com.example.SpotifyConverter.infra.repositories;

import com.example.SpotifyConverter.entities.SpotifyTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotifyTrackRepository extends JpaRepository<SpotifyTrack, String> {
}
