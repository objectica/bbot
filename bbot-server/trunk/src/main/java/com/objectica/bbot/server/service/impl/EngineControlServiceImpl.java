package com.objectica.bbot.server.service.impl;

import com.objectica.bbot.server.service.EngineControlService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.io.IOException;

/**
 * @author Serge Podgurskiy
 */
public class EngineControlServiceImpl implements EngineControlService
{
    private WebResource webResource;

    public EngineControlServiceImpl(String rootUrl)
    {
        Client client = new Client();
        webResource = client.resource(rootUrl + "/engine");
    }

    @Override
    public void sendCommand(int engine, int command, byte speed) throws IOException
    {
        MultivaluedMapImpl paramertes = new MultivaluedMapImpl();
        paramertes.add("engine", engine);
        paramertes.add("command", command);
        paramertes.add("speed", speed);
        webResource.queryParams(paramertes).get(String.class);
    }

    public void configure(int engine, int command, byte speed) throws IOException
    {
        MultivaluedMapImpl paramertes = new MultivaluedMapImpl();
        paramertes.add("engine", engine);
        paramertes.add("command", command);
        paramertes.add("speed", speed);
        webResource.queryParams(paramertes).get(String.class);
    }
}
