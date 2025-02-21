package com.example.SpotifyConverter.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_spotify_track")
public class SpotifyTrack {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "music_name")
    private String musicName;
    @ElementCollection
    @Column(name = "artists_name")
    private List<String> artistsName;
    @Column(name = "album_name")
    private String albumsName;
    @Column(name = "youtube_link")
    private String youtubeLink;

    public  SpotifyTrack(String id, String musicName, List<String> artistsName, String albumsName) throws Exception {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("O id não pode ser nulo ou vazio");
        }
        this.id = id;
        if (musicName == null || musicName.isBlank()) {
            throw new IllegalArgumentException("O nome da musica não pode ser nulo ou vazio");
        }
        this.musicName = musicName;

        if (artistsName == null || artistsName.isEmpty()) {
            throw new IllegalArgumentException("O nome do artista não pode ser nulo ou vazio");
        }
        this.artistsName = artistsName;
        this.albumsName = albumsName;
    }

    public SpotifyTrack() {

    }

    public String getId() {
        return this.id;
    }

    public String getMusicName() {
        return this.musicName.replace(" ", "+");
    }

    public List<String> getArtistsName() {
        return this.artistsName.stream().map(artist -> artist.replace(" ", "+")).toList();
    }

    public String getAlbumsName() {
        return this.albumsName.replace(" ", "+");
    }

    public void setYoutubeLink(String youtubeLink) {
        if (youtubeLink == null || youtubeLink.isBlank()) {
            throw new IllegalArgumentException("O link do YouTube não pode ser nulo ou vazio.");
        }
        this.youtubeLink = youtubeLink;
    }
    public String getYoutubeLink() {
        return  this.youtubeLink;
    }
}
