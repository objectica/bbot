package com.objectica.bbot.missile;

import ch.ntb.usb.USBException;
import org.junit.Test;

public class TestMissileController {

    @Test
    public void runTest() throws USBException, InterruptedException {
        MissileController controller = new MissileController();
        controller.init();
        controller.executeCommand(MissileCommand.LEFT, 1000);
        controller.executeCommand(MissileCommand.UP, 1000);
        controller.executeCommand(MissileCommand.DOWN, 1000);
        controller.executeCommand(MissileCommand.STOP, null);
        controller.closeDevice();
    }
}
