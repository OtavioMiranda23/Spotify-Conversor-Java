package com.example.SpotifyConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
class SpotifyConverterApplicationTests {

	@Test
	void convertToYoutube () throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest requestToken = HttpRequest.newBuilder(URI.create("http://localhost:8080/auth")).build();
		HttpResponse<String> responseToken = client.send(requestToken, HttpResponse.BodyHandlers.ofString());
		assertNotNull(responseToken.body());

		String trackId = "1cM4eMzeqalRs8HbXtfT9X";
		HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(trackId);
		HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8080/convert/youtube")).POST(bodyPublisher).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
		LinkedList<String> artistName = new LinkedList<>();
		artistName.add("João Gilberto");
		Map<String, Object> expectResponse = new HashMap<>();
		expectResponse.put("albumName", "Chega de Saudade / O Amor o Sorriso e a Flor / João Gilberto (1961) [Ultimate Mix]");
		expectResponse.put("artistsName", artistName);
		expectResponse.put("id", "1cM4eMzeqalRs8HbXtfT9X");
		expectResponse.put("musicName", "Chega de Saudade - Ultimate Mix");
		ObjectMapper objectMapper = new ObjectMapper();
		String expectedJson = objectMapper.writeValueAsString(expectResponse);
        assertEquals(expectedJson, response.body());
	}

}
