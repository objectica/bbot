package com.objectica.bbot.desktop.rumblepad;

import ch.ntb.usb.Device;
import ch.ntb.usb.USB;
import com.objectica.bbot.rumblepad.GamePadReadThread;
import com.objectica.bbot.rumblepad.listeners.ControlListener;

import java.io.IOException;

/**
 * @author Sergey Podgurskiy
 */
public class RumblepadControll
{
    public static final short VENDOR_ID = 0x46D;
    public static final short PRODUCT_ID = (short) 0xC218;
    public static final int CONFIGURATION = 1;
    public static final int INTERFACE = 0;

    public static void main(String[] args) throws IOException
    {
        Device device = USB.getDevice(VENDOR_ID, PRODUCT_ID);
        GamePadReadThread gamePadThread = new GamePadReadThread(device, CONFIGURATION, INTERFACE);
        ControlListener listener = new EngineControlListener();
        gamePadThread.setListener(listener);

        Thread thread = new Thread(gamePadThread);
        thread.start();

        System.in.read();
        gamePadThread.stop();
    }
}
