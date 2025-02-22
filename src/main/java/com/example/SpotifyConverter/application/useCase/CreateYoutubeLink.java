package com.example.SpotifyConverter.application.useCase;

import com.example.SpotifyConverter.entities.SpotifyTrack;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.SpotifyConverter.infra.exceptions.ExternalServiceException;
import com.example.SpotifyConverter.infra.exceptions.ResourceNotFoundException;
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

    public SpotifyTrack execute(String musicId, int siteToConvert, String musicName, List<String> artistsFounded, String albumName) throws Exception {
        Optional<SpotifyTrack> trackFinded = repository.findByIdSpotify(musicId);
        if (trackFinded.isPresent()) {
            System.out.println("Recuperado do Banco");
            var track = trackFinded.get();
            track.setYoutubeLink(track.getYoutubeLink(), siteToConvert);
            return track;
        }
        try {
            HttpClient client = HttpClient.newHttpClient();
            SpotifyTrack spotifyTrack = new SpotifyTrack(musicId, musicName, artistsFounded, albumName);
            String uri = this.baseGoogleSearchUrl + spotifyTrack.getMusicName() + spotifyTrack.getArtistsName() + spotifyTrack.getAlbumsName() + "&tbm=vid";
            HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ExternalServiceException("Erro ao pesquisar track" + response.statusCode());
            }
            Document doc = Jsoup.parse(response.body());
            String youtubeMusicLink = this.extractLink(doc);
            spotifyTrack.setYoutubeLink(youtubeMusicLink, siteToConvert);
            return repository.save(spotifyTrack);
        }
        catch (IOException | InterruptedException e) {
            throw new ExternalServiceException("Erro ao conectar ao serviço externo", e);
        }
        catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro inesperado ao processar a requisição", e);
        }
    }

    private String extractLink(Document doc) {
        List<String> youtubeLinks = doc.select("a[href]")
                .stream()
                .map(link -> link.attr("href"))
                .filter(href -> href.contains("youtube.com/watch"))
                .map(href -> href.startsWith("/url?q=") ? href.substring(7).split("&")[0] : href)
                .collect(Collectors.toList());
        if (youtubeLinks.isEmpty()) {
            throw new ResourceNotFoundException("O link do youtube não foi encontrado");
        }
        return youtubeLinks.getFirst();
    }
}
