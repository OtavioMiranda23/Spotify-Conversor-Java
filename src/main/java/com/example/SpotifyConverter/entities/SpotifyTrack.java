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
        if (id == null) {
            throw new Exception("id is null");
        }
        this.id = id;
        if (musicName == null) {
            throw new Exception("music name is null");
        }
        this.musicName = musicName;

        if (artistsName.isEmpty()) {
            this.artistsName = artistsName;
        }
        this.artistsName = artistsName;
        if (albumsName == null) {
            this.albumsName = albumsName;
        }
        this.albumsName = albumsName;
    }

    public SpotifyTrack() {

    }

    public Map<String, Object> toMap()  {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("musicName", this.musicName);
        map.put("artistsName", this.artistsName);
        map.put("albumName", this.albumsName);

        return map;
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

    public void setYoutubeLink(String youtubeLink) throws Exception {
        if (youtubeLink.length() < 1) {
            throw new Exception("Youtube link not be empty");
        }
        this.youtubeLink = youtubeLink;
    }
}
