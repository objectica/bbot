package com.objectica.bbot.engine.service;

import com.objectica.bbot.engine.service.exceptions.EngineConnectionException;

import java.io.IOException;

public interface SerialDeviceHandler {

    public void write(byte... data) throws IOException;

    public void close() throws IOException;

    public String getPortName();

    public void open(String portName) throws EngineConnectionException;
}
