package com.phrase.tms.assignment.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phrase.tms.assignment.model.Login;
import com.phrase.tms.assignment.model.ProjectList;
import com.phrase.tms.assignment.model.TMSToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class TMSClientTest {
    private final static String auth = "https://test.com/auth";
    private final static String projects = "https://test.com/projects";
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpClient httpClient;
    @InjectMocks
    private TMSClient client;

    @Mock
    private HttpResponse<String> response;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(client, "auth", auth);
        ReflectionTestUtils.setField(client, "projects", projects);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void auth() throws IOException, InterruptedException {
        //given
        var login = new Login();
        var loginStr = "loginStr";
        var token = new TMSToken();
        when(objectMapper.writeValueAsString(login)).thenReturn(loginStr);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(objectMapper.readValue(response.body(), TMSToken.class)).thenReturn(token);

        //when
        var result = client.auth(login);

        //then
        assertEquals(result, token);

        //given
        when(response.statusCode()).thenReturn(401);

        //then
        assertThrows(AuthenticationException.class, () -> client.auth(login));
    }

    @Test
    void getProjects() throws IOException, InterruptedException {
        //given
        var projectList = new ProjectList();
        var token = "token";

        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(objectMapper.readValue(response.body(), ProjectList.class)).thenReturn(projectList);

        //when
        var result = client.getProjects("token");

        //then
        assertEquals(result, projectList);

        //given
        when(response.statusCode()).thenReturn(401);

        //then
        assertThrows(AuthenticationException.class, () -> client.getProjects(token));

        //given
        when(response.statusCode()).thenReturn(500);

        //then
        assertThrows(TMSClientException.class, () -> client.getProjects(token));
    }
}