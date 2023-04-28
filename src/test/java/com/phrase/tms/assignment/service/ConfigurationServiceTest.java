package com.phrase.tms.assignment.service;

import com.phrase.tms.assignment.client.AuthenticationException;
import com.phrase.tms.assignment.client.TMSClient;
import com.phrase.tms.assignment.model.Login;
import com.phrase.tms.assignment.model.TMSToken;
import com.phrase.tms.assignment.repository.ConfigurationRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class ConfigurationServiceTest {
    private final static String auth = "https://test.com/auth";
    private final static String projects = "https://test.com/projects";
    @Mock
    private TMSClient client;
    @Mock
    private ConfigurationRepository repository;
    @InjectMocks
    private ConfigurationService configurationService;
    @Mock
    private HttpServletResponse response;

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
    void saveConfig() {
        //given
        var login = new Login();
        var token = new TMSToken();
        when(client.auth(login)).thenReturn(token);

        //when
        configurationService.saveConfig(login, response);

        //then
        Mockito.verify(response).addCookie(any(Cookie.class));
        Mockito.verify(repository).saveOrUpdate(login.getUserName(), login.getPassword(), true);
        Mockito.verifyNoMoreInteractions(response, repository);

        //given
        when(client.auth(login)).thenThrow(AuthenticationException.class);

        //when
        assertThrows(ConfigException.class, () -> configurationService.saveConfig(login, response));

        //then
        Mockito.verify(response).addCookie(any(Cookie.class));
        Mockito.verify(repository).saveOrUpdate(login.getUserName(), login.getPassword(), false);
        Mockito.verifyNoMoreInteractions(response, repository);
    }
}