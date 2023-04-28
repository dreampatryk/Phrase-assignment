package com.phrase.tms.assignment.controller;

import com.phrase.tms.assignment.model.Login;
import com.phrase.tms.assignment.service.ConfigException;
import com.phrase.tms.assignment.service.ConfigurationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ConfigurationController {
    private final ConfigurationService configurationService;

    @PostMapping(value = "/save-config", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveConfig(@RequestBody Login login, HttpServletResponse response) {
        configurationService.saveConfig(login, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(ConfigException.class)
    public ResponseEntity<Error> handleConfigException() {
        return new ResponseEntity<>(new Error("Failed to activate the configuration"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
