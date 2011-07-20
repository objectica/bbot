package com.objectica.bbot.engine.service.impl;

import com.objectica.bbot.engine.service.SerialPortListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class SerialReadDispatcher implements Runnable {
    private Logger logger = LoggerFactory.getLogger(SerialReadDispatcher.class);
    private Set<SerialPortListener> listeners = new HashSet<SerialPortListener>();
    private InputStream stream;
    private boolean terminated = false;

    public SerialReadDispatcher(InputStream stream) {
        this.stream = stream;
    }

    public void run() {
        StringBuffer valueBuffer = new StringBuffer();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = this.stream.read(buffer)) > -1 && !terminated) {
                valueBuffer.append(new String(buffer, 0, len));
                if (valueBuffer.indexOf("\n") != -1) {
                    String value = valueBuffer.substring(0, valueBuffer.indexOf("\n") - 1);
                    logger.debug("Read: '{}'", value);
                    String data = valueBuffer.toString();
                    valueBuffer = new StringBuffer(data.substring(valueBuffer.indexOf("\n") + 1));
                    // Notify listeners
                    if (!listeners.isEmpty()) {
                        for (SerialPortListener listener : listeners) {
                            listener.stringRead(value);
                        }
                    }
                }
            }
        }
        catch (IOException e) {
            logger.error("Exception in serial logger thread", e);
        }
    }

    public void addListener(SerialPortListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(SerialPortListener listener) {
        this.listeners.remove(listener);
    }

    public void stop() {
        terminated = true;
    }
}
