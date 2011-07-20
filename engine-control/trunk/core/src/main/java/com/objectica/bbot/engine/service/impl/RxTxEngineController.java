package com.objectica.bbot.engine.service.impl;

import com.objectica.bbot.engine.service.EngineController;
import com.objectica.bbot.engine.service.SerialPortListener;
import com.objectica.bbot.engine.service.commands.ControllerCommand;
import com.objectica.bbot.engine.service.exceptions.EngineConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RxTxEngineController implements EngineController
{
    private static final Logger logger = LoggerFactory.getLogger(RxTxEngineController.class);
    private RxTxDeviceHandler deviceHandler;

    public RxTxEngineController(String portNumber, int connectTimeout, boolean testMode) throws EngineConnectionException, IOException
    {
        deviceHandler = new RxTxDeviceHandler(testMode);
        deviceHandler.open(portNumber);
        waitForInit(deviceHandler, connectTimeout);
    }

    protected void waitForInit(final RxTxDeviceHandler deviceHandler, int timeout) throws EngineConnectionException
    {
        final Boolean[] initialized = {false};
        deviceHandler.addPortListener(new SerialPortListener()
        {
            public void stringRead(String value)
            {
                initialized[0] = true;
                deviceHandler.removePortListener(this);
            }
        });
        long startTime = System.currentTimeMillis();
        while (startTime - System.currentTimeMillis() < timeout)
        {
            if (initialized[0])
            {
                return;
            }
            try
            {
                Thread.sleep(50);
            } catch (InterruptedException e)
            {
                // ignore
            }
        }
        throw new EngineConnectionException("Timeout exception");
    }

    public void sendCommand(int engine, ControllerCommand command, byte speed) throws IOException
    {
        byte[] byteCommand = new byte[3];
        byteCommand[0] = new Integer(engine).byteValue();
        byteCommand[1] = command.getCommand();
        byteCommand[2] = speed;
        deviceHandler.write(byteCommand);
    }
}
