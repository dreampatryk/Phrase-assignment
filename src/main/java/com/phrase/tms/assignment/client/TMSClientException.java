package com.phrase.tms.assignment.client;

public class TMSClientException extends RuntimeException {
    public TMSClientException(String message) {
        super(message);
    }

    public TMSClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
