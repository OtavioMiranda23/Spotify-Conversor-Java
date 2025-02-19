package com.example.SpotifyConverter.application.useCase;

import com.example.SpotifyConverter.entities.SpotifyTrack;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.example.SpotifyConverter.infra.repositories.SpotifyTrackRepository;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateYoutubeLink {
    private String baseGoogleSearchUrl = "https://www.google.com/search?q=";
    private SpotifyTrackRepository repository;

    @Autowired
    public  CreateYoutubeLink(SpotifyTrackRepository repository) {
        this.repository = repository;
    }

    public void execute(String musicId, String musicName, ArrayList<String> artistsFounded, String albumName) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        SpotifyTrack spotifyTrack = new SpotifyTrack(musicId, musicName, artistsFounded, albumName);
        String uri = this.baseGoogleSearchUrl + spotifyTrack.getMusicName() + spotifyTrack.getArtistsName() + spotifyTrack.getAlbumsName() + "&tbm=vid";
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Document doc = Jsoup.parse(response.body());
        List<String> youtubeLinks = doc.select("a[href]")
                .stream()
                .map(link -> link.attr("href"))  // Extrai o valor do atributo href
                .filter(href -> href.contains("youtube.com/watch"))  // Filtra apenas links do YouTube
                .map(href -> href.startsWith("/url?q=") ? href.substring(7).split("&")[0] : href)  // Remove prefixos do Google
                .collect(Collectors.toList());
        String youtubeMusicLink = youtubeLinks.getFirst();
        try {
            spotifyTrack.setYoutubeLink(youtubeMusicLink);
            repository.save(spotifyTrack);
            List<SpotifyTrack> musics = repository.findAll();
            musics.forEach(m -> System.out.println(m.getId() + m.getAlbumsName() + m.getArtistsName() + m.getMusicName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
