package com.example.SpotifyConverter.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SpotifyTrack {
    private String musicName;
    private LinkedList<String> artistsName;
    private String albumsName;

    public  SpotifyTrack(String id, String musicName, LinkedList<String> artistsName, String albumsName) throws Exception {
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

    public Map<String, Object> toMap()  {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("musicName", this.musicName);
        map.put("artistsName", this.artistsName);
        map.put("albumName", this.albumsName);

        return map;
    }
    private String id;

    public String getId() {
        return this.id;
    }

    public String getMusicName() {
        return this.musicName;
    }

    public LinkedList<String> getArtistsName() {
        return this.artistsName;
    }

    public String getAlbumsName() {
        return this.albumsName;
    }
}
