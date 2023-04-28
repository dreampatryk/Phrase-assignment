package com.phrase.tms.assignment.controller;

import com.phrase.tms.assignment.Constants;
import com.phrase.tms.assignment.client.AuthenticationException;
import com.phrase.tms.assignment.client.TMSClient;
import com.phrase.tms.assignment.client.TMSClientException;
import com.phrase.tms.assignment.model.Error;
import com.phrase.tms.assignment.model.ProjectList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class ProjectController {
    private final TMSClient client;

    @GetMapping("/projects")
    public ResponseEntity<ProjectList> getProjects(@CookieValue(name = Constants.tmsCookie) String token) {
        var projects = client.getProjects(token);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Error> handleAuthenticationException() {
        return new ResponseEntity<>(new Error("Failed to authenticate to TMS Client"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TMSClientException.class)
    public ResponseEntity<Error> handleTMSException() {
        return new ResponseEntity<>(new Error("An unexpected error has occurred. Try again later."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
