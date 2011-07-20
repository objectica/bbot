package com.objectica.bbot.engine.web;

import com.objectica.bbot.engine.service.EngineController;
import com.objectica.bbot.engine.service.commands.ControllerCommand;
import com.objectica.bbot.engine.service.exceptions.EngineConnectionException;
import com.objectica.bbot.engine.service.impl.EngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;

@Path("/engine")
public class EngineServiceResource
{
    private static final Logger logger = LoggerFactory.getLogger(EngineServiceResource.class);
    private static final EngineController engineController;

    static
    {
        engineController = EngineFactory.createEngineController();
    }

    @GET
    public String getGreeting(@QueryParam("engine") int engine, @QueryParam("command") int command, @QueryParam("speed") int speed) throws IOException, EngineConnectionException
    {
        logger.info("Received ENGINE:{} COMMAND:{} VALUE:{}", new Object[]{engine, command, speed});
        synchronized (engineController)
        {
            engineController.sendCommand(engine, ControllerCommand.fromValue((byte) command), (byte) speed);
        }
        return "Hi there";
    }
}
