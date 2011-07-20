package com.objectica.bbot.engine.service.impl;

import com.objectica.bbot.engine.service.SerialDeviceHandler;
import com.objectica.bbot.engine.service.SerialPortListener;
import com.objectica.bbot.engine.service.exceptions.EngineConnectionException;
import com.objectica.bbot.engine.utils.HexUtils;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RxTxDeviceHandler implements SerialDeviceHandler
{
    private static final Logger logger = LoggerFactory.getLogger(RxTxDeviceHandler.class);
    private String portName;
    private OutputStream outputStream;
    private SerialReadDispatcher inputLoggerThread;
    private boolean testMode = false;

    public RxTxDeviceHandler(boolean testMode)
    {
        this.testMode = testMode;
        this.portName = "TestPORT";
    }

    public void write(byte... data) throws IOException
    {
        logger.info("Send {} to {}", HexUtils.toByteString(data), getPortName());
        if (!testMode)
        {
            outputStream.write(data);
        }
    }

    public void close() throws IOException
    {
        outputStream.close();
        inputLoggerThread.stop();
    }

    public void addPortListener(SerialPortListener listener){
        if (inputLoggerThread==null)
        {
            throw new RuntimeException("Port should be opened before adding listeners");
        }
        inputLoggerThread.addListener(listener);
    }

    public void removePortListener(SerialPortListener listener){
        if (inputLoggerThread!=null)
        {
            inputLoggerThread.removeListener(listener);
        }
    }

    public String getPortName()
    {
        return portName;
    }

    public void open(String portName) throws EngineConnectionException
    {
        if (testMode)
        {
            return;
        }
        this.portName = portName;
        try
        {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            if (portIdentifier.isCurrentlyOwned())
            {
                throw new EngineConnectionException("Port is currently in use");
            } else
            {
                CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

                if (commPort instanceof SerialPort)
                {
                    SerialPort serialPort = (SerialPort) commPort;
                    serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                    InputStream in = serialPort.getInputStream();
                    outputStream = serialPort.getOutputStream();

                    inputLoggerThread = new SerialReadDispatcher(in);
                    (new Thread(inputLoggerThread)).start();
                } else
                {
                    throw new EngineConnectionException("Only serial ports could be handled");
                }
            }
        } catch (Throwable tr)
        {
            throw new EngineConnectionException("Exception in opening port", tr);
        }
    }
}
