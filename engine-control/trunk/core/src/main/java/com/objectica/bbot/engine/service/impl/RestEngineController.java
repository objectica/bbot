package com.objectica.bbot.engine.service.impl;

import com.objectica.bbot.engine.service.EngineController;
import com.objectica.bbot.engine.service.commands.ControllerCommand;
import com.objectica.bbot.engine.service.exceptions.EngineConnectionException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.io.IOException;

public class RestEngineController implements EngineController
{
    private WebResource webResource;

    public RestEngineController(String rootUrl)
    {
        Client client = new Client();
        webResource = client.resource(rootUrl + "/engine");
    }

    public void sendCommand(int engine, ControllerCommand command, byte speed) throws EngineConnectionException, IOException
    {
        MultivaluedMapImpl paramertes = new MultivaluedMapImpl();
        paramertes.add("engine", engine);
        paramertes.add("command", (int) command.getCommand());
        paramertes.add("speed", speed);
        webResource.queryParams(paramertes).get(String.class);
    }
}
