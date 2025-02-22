package com.example.SpotifyConverter.entities;
import java.util.*;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_spotify_track")
public class SpotifyTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "id_spotify", nullable = false, unique = true)
    private String idSpotify;
    @Column(name = "music_name")
    private String musicName;
    @ElementCollection
    @Column(name = "artists_name")
    private List<String> artistsName;
    @Column(name = "album_name")
    private String albumsName;
    @Column(name = "youtube_link")
    private String youtubeLink;
    public  SpotifyTrack(String idSpotify, String musicName, List<String> artistsName, String albumsName) throws Exception {
        if (idSpotify == null || idSpotify.isBlank()) {
            throw new IllegalArgumentException("O idSpotify não pode ser nulo ou vazio");
        }
        if (musicName == null || musicName.isBlank()) {
            throw new IllegalArgumentException("O nome da musica não pode ser nulo ou vazio");
        }
        if (artistsName == null || artistsName.isEmpty()) {
            throw new IllegalArgumentException("O nome do artista não pode ser nulo ou vazio");
        }
        this.idSpotify = idSpotify;
        this.artistsName = artistsName;
        this.musicName = musicName;
        this.albumsName = albumsName;
    }

    public SpotifyTrack() {}

    public String getidSpotify() {
        return this.idSpotify;
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

    public void setYoutubeLink(String youtubeLink, int siteToConvert) {
        if (youtubeLink == null || youtubeLink.isBlank()) {
            throw new IllegalArgumentException("O link do YouTube não pode ser nulo ou vazio.");
        }
        System.out.println(siteToConvert);
        if (siteToConvert == 1){
            this.youtubeLink = this.youtubeLink.replace("://www", "://music");
            return;
        }
        this.youtubeLink = youtubeLink;
    }
    public String getYoutubeLink() { return  this.youtubeLink; }
}
