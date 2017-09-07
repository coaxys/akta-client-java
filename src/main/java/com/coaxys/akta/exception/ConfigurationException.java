package com.coaxys.akta.exception;

import java.io.IOException;

public class ConfigurationException extends Exception {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, IOException exception) {
        super(message, exception);
    }
}
