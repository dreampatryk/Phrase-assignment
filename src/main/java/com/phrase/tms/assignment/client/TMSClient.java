package com.phrase.tms.assignment.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phrase.tms.assignment.model.Login;
import com.phrase.tms.assignment.model.ProjectList;
import com.phrase.tms.assignment.model.TMSToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
public class TMSClient {
    private static final int OK = 200;

    @Value("${tms.auth}")
    private String auth;
    @Value("${tms.projects}")
    private String projects;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    public TMSToken auth(Login login) {
        try {
            String requestBody = objectMapper.writeValueAsString(login);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(auth))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != OK) {
                throw new AuthenticationException("Failed to authenticate: " + response.body());
            }
            return objectMapper.readValue(response.body(), TMSToken.class);
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProjectList getProjects(String token) {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(new URI(projects))
                    .header("Authorization", "ApiToken " + token)
                    .GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 401) {
                throw new AuthenticationException("Failed to authenticate");
            }
            if (response.statusCode() != OK) {
                throw new TMSClientException("An error has occurred while invoking TMS Client: " + response.statusCode());
            }
            return objectMapper.readValue(response.body(), ProjectList.class);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new TMSClientException("An error has occurred while invoking TMS Client", e);
        }
    }
}
