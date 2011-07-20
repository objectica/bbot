package com.objectica.bbot.engine.service.exceptions;

public class EngineConnectionException extends Exception{
    public EngineConnectionException() {
    }

    public EngineConnectionException(String message) {
        super(message);
    }

    public EngineConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EngineConnectionException(Throwable cause) {
        super(cause);
    }
}
