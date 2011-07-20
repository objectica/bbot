package com.objectica.bbot.rumblepad;

import ch.ntb.usb.Device;
import ch.ntb.usb.USBException;
import ch.ntb.usb.USBTimeoutException;
import com.objectica.bbot.rumblepad.actions.ControlAction;
import com.objectica.bbot.rumblepad.listeners.ControlListener;
import com.objectica.bbot.rumblepad.parser.RumblepadActionParser;
import com.objectica.bbot.rumblepad.utils.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class GamePadReadThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(GamePadReadThread.class);
    private final Device device;
    private ControlListener listener;
    private boolean interrupted = false;
    private RumblepadActionParser parser;

    public GamePadReadThread(Device device, int configuration, int interfaceConfig) throws USBException {
        parser = new RumblepadActionParser();
        this.device = device;
        this.device.open(configuration, interfaceConfig, 0);
    }

    public void run() {
        try {
            while (!interrupted) {
                try {
                    byte[] data = read(device, 0x81);
                    if (listener != null && data != null) {
                        Collection<ControlAction> actions = parseData(data);
                        if (!actions.isEmpty()) {
                            for (ControlAction action : actions) {
                                listener.actionFired(action);
                            }
                        }
                    }
                } catch (USBException e) {
                    logger.error("Exception in reading from device", e);
                    interrupted = true;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        } finally {
            try {
                device.close();
            } catch (USBException e) {
                logger.error("Exception in closing device", e);
            }
        }
        logger.info("Rumblepad read thread stopped");
    }

    protected Collection<ControlAction> parseData(byte[] data) {
        if (logger.isDebugEnabled()) {
            logger.debug(HexUtils.toByteString(data) + "\t" + HexUtils.toHexString(data));
        }
        return parser.parseActions(data);
    }

    public byte[] read(Device device, int endpointAddress) throws USBException {
        if (device.getMaxPacketSize() <= 0) {
            logger.error("Max packet size is: " + device.getMaxPacketSize());
            return null;
        }
        byte[] data = new byte[device.getMaxPacketSize()];
        try {
            device.readInterrupt(endpointAddress, data, device.getMaxPacketSize(), 5000, false);
        } catch (USBTimeoutException e) {
            logger.debug("USB read timeout");
            return null;
        }
        return data;
    }

    public void stop() {
        this.interrupted = true;
    }

    public void setListener(ControlListener listener) {
        this.listener = listener;
    }
}
