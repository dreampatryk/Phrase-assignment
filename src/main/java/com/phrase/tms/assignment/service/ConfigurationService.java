package com.phrase.tms.assignment.service;

import com.phrase.tms.assignment.Constants;
import com.phrase.tms.assignment.client.AuthenticationException;
import com.phrase.tms.assignment.client.TMSClient;
import com.phrase.tms.assignment.model.Login;
import com.phrase.tms.assignment.repository.ConfigurationRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ConfigurationService {
    private final ConfigurationRepository repository;
    private final TMSClient tmsClient;

    public void saveConfig(Login login, HttpServletResponse response) {
        boolean active = false;
        try {
            var tmsToken = tmsClient.auth(login);
            var cookie = new Cookie(Constants.tmsCookie, tmsToken.getToken());
            response.addCookie(cookie);
            active = true;
        } catch (AuthenticationException e) {
            log.error("Failed to activate the configuration: {}", e.getMessage());
        }
        repository.saveOrUpdate(login.getUserName(), login.getPassword(), active);
        log.info("The configuration for {} has been saved", login.getUserName());

        if (!active) {
            throw new ConfigException("Failed to activate the configuration");
        }
    }
}
