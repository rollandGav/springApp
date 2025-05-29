package com.springApp.jpa.exception;

public class EmailSendingException extends Exception {

    public EmailSendingException(String message) {
        super(message);
    }
}
