package com.example.SpotifyConverter;

import com.example.SpotifyConverter.application.useCase.GetLinkById;
import com.example.SpotifyConverter.entities.SpotifyTrack;
import com.example.SpotifyConverter.infra.repositories.SpotifyTrackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpotifyConverterApplicationTests {
	@Autowired
	private SpotifyTrackRepository repository;

	@Test
	void convertToYoutube () throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		String trackId = "https://open.spotify.com/intl-pt/track/3vNwY4KaBRnJP4G6UBJPZH";
		var bodyMap = new HashMap<>();
		bodyMap.put("link", trackId);
		bodyMap.put("site", 0);
		var objectMapper  = new ObjectMapper();
		objectMapper.writeValueAsString(bodyMap);
		String requestBodyJson = objectMapper.writeValueAsString(bodyMap);
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/convert/youtube"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		LinkedList<String> artistName = new LinkedList<>();
		artistName.add("João Gilberto");
		Map<String, Object> expectResponse = new HashMap<>();
		expectResponse.put("idMusic", "1cM4eMzeqalRs8HbXtfT9X");
		expectResponse.put("link", "https://www.youtube.com/watch%3Fv%3Dtlp8iY4g--4");
		ObjectMapper objectMapper1 = new ObjectMapper();
		Map<String, Object> mapResponse = objectMapper1.readValue(response.body(), Map.class);
        assertEquals(expectResponse.get("link"), mapResponse.get("link"));
	}
}
