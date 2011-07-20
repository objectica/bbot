package com.objectica.bbot.engine.service;

import com.objectica.bbot.engine.service.commands.ControllerCommand;
import com.objectica.bbot.engine.service.exceptions.EngineConnectionException;

import java.io.IOException;

public interface EngineController {

    public void sendCommand(int engine, ControllerCommand command, byte speed) throws EngineConnectionException, IOException;
}
