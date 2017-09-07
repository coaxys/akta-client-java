package com.coaxys.akta.exception;

import java.io.IOException;

public class AktaException extends Exception {

    public AktaException(String message) {
        super(message);
    }

    public AktaException(String message, IOException exception) {
        super(message, exception);
    }

}
