package com.objectica.bbot.missile;

import ch.ntb.usb.Device;
import ch.ntb.usb.USB;
import ch.ntb.usb.USBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MissileController {
    private static final Logger logger = LoggerFactory.getLogger(MissileController.class);

    private static final short VENDOR_ID = 0x1130;
    private static final short PRODUCT_ID = (short) 0x0202;
    private static final int CONFIGURATION = 1;
    private static final int INTERFACE = 0;

    private final static byte[] PACKET_INIT_A = new byte[]{'U', 'S', 'B', 'C', 0, 0, 4, 0};
    private final static byte[] PACKET_INIT_B = new byte[]{'U', 'S', 'B', 'C', 0, 0x40, 2, 0};
    private final static byte[] PACKET_COMMAND = new byte[64];

    private Device device;

    public void init() throws USBException {
        device = USB.getDevice(VENDOR_ID, PRODUCT_ID);
        device.open(CONFIGURATION, INTERFACE, INTERFACE);
    }

    public void closeDevice() throws USBException {
        device.close();
    }

    public void executeCommand(MissileCommand command, Integer stopAfter) throws USBException {
        if (device == null) {
            throw new USBException("Device is not initialized");
        }
        byte[] packetArray = new byte[PACKET_COMMAND.length];
        packetArray[1] = ((command.getCommand() & MissileCommand.LEFT.getCommand()) > 0) ? (byte) 1 : 0;
        packetArray[2] = ((command.getCommand() & MissileCommand.RIGHT.getCommand()) > 0) ? (byte) 1 : 0;
        packetArray[3] = ((command.getCommand() & MissileCommand.UP.getCommand()) > 0) ? (byte) 1 : 0;
        packetArray[4] = ((command.getCommand() & MissileCommand.DOWN.getCommand()) > 0) ? (byte) 1 : 0;
        packetArray[5] = ((command.getCommand() & MissileCommand.FIRE.getCommand()) > 0) ? (byte) 1 : 0;
        packetArray[6] = 8;
        packetArray[7] = 8;
        sendPacket(PACKET_INIT_A);
        sendPacket(PACKET_INIT_B);
        sendPacket(packetArray);
        if (stopAfter != null) {
            try {
                Thread.sleep(stopAfter);
            } catch (InterruptedException e) {
                // ignore
            }
            executeCommand(MissileCommand.STOP, null);
        }
    }

    protected void sendPacket(byte[] packet) throws USBException {
        if (logger.isDebugEnabled()) {
            StringBuffer hexBuffer = new StringBuffer();
            for (byte aData : packet) {
                hexBuffer.append(aData).append(" ");
            }
            logger.debug("Sending controll message" + hexBuffer.toString());
        }
        device.controlMsg(0x21, 0x9, 2, 1, packet, packet.length, 2000, false);
    }

}
