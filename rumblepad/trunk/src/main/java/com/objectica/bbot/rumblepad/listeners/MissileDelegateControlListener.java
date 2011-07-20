package com.objectica.bbot.rumblepad.listeners;

import ch.ntb.usb.USBException;
import com.objectica.bbot.rumblepad.actions.ButtonAction;
import com.objectica.bbot.rumblepad.actions.ControlAction;
import com.objectica.bbot.rumblepad.actions.JoypadAction;
import com.objectica.bbot.missile.MissileCommand;
import com.objectica.bbot.missile.MissileController;

public class MissileDelegateControlListener implements ControlListener {
    private MissileController controller;
    private static final int MOVE_TIMEOUT = 100;

    public MissileDelegateControlListener() {
        controller = new MissileController();
        try {
            controller.init();
        } catch (USBException e) {
            throw new RuntimeException(e);
        }
    }

    public void actionFired(ControlAction action) {
        try {
            if (action instanceof JoypadAction) {
                JoypadAction moveAction = ((JoypadAction) action);
                if (moveAction.getNumber() == 2) {
                    if (moveAction.getVertical() < 0) {
                        controller.executeCommand(MissileCommand.DOWN, MOVE_TIMEOUT);
                    } else if (moveAction.getVertical() > 0) {
                        controller.executeCommand(MissileCommand.UP, MOVE_TIMEOUT);
                    } else if (moveAction.getHorizontal() < 0) {
                        controller.executeCommand(MissileCommand.LEFT, MOVE_TIMEOUT);
                    } else if (moveAction.getHorizontal() > 0) {
                        controller.executeCommand(MissileCommand.RIGHT, MOVE_TIMEOUT);
                    }
                }
            } else if (action instanceof ButtonAction) {
                ButtonAction buttonAction = ((ButtonAction) action);
                if (4 == buttonAction.getNumber()) {
                    controller.executeCommand(MissileCommand.FIRE, 100);
                }
            }
        } catch (USBException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() throws USBException {
        controller.closeDevice();
    }
}
