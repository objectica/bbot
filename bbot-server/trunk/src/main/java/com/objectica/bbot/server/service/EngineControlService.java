package com.objectica.bbot.server.service;

import java.io.IOException;

/**
 * @author Serge Podgurskiy
 */
public interface EngineControlService
{
    void sendCommand(int engine, int command, byte speed) throws IOException;
}
