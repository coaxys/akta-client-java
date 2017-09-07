package com.coaxys.akta.exception;

public class AktaException extends Exception {

    public AktaException(String message) {
        super(message);
    }

    public AktaException(String message, Exception exception) {
        super(message, exception);
    }

}
