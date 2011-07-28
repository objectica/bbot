package com.objectica.bbot.engine.web;

import com.objectica.bbot.engine.service.EngineController;
import com.objectica.bbot.engine.service.commands.ControllerCommand;
import com.objectica.bbot.engine.service.exceptions.EngineConnectionException;
import com.objectica.bbot.engine.service.impl.EngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;

@Path("/engine")
public class EngineServiceResource
{
    private static final Logger logger = LoggerFactory.getLogger(EngineServiceResource.class);
    private static EngineController engineController;

    @POST
    public String configure(@QueryParam("portName") String portName, @QueryParam("connectTimeout") int connectionTimeout, @QueryParam("testMode") boolean testMode)
    {
        logger.info("Received Configure command. PortName: {}, Timeout: {}, testMode: {}", new Object[]{portName, connectionTimeout, testMode});
        synchronized (this)
        {
            try
            {
                engineController = EngineFactory.createEngineController(portName, connectionTimeout, testMode);
            } catch (Exception e)
            {
                logger.error("Error in configure", e);
                return e.getMessage();
            }
        }
        return null;
    }

    @GET
    public String sendCommand(@QueryParam("engine") int engine, @QueryParam("command") int command, @QueryParam("speed") int speed) throws IOException, EngineConnectionException
    {
        logger.info("Received ENGINE:{} COMMAND:{} VALUE:{}", new Object[]{engine, command, speed});
        synchronized (this)
        {
            if (engineController == null)
            {
                engineController = EngineFactory.createEngineController();
            }
            engineController.sendCommand(engine, ControllerCommand.fromValue((byte) command), (byte) speed);
        }
        return "Hi there";
    }
}
