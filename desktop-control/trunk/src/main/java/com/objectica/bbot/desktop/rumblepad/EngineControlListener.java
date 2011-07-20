package com.objectica.bbot.desktop.rumblepad;

import com.objectica.bbot.engine.service.EngineController;
import com.objectica.bbot.engine.service.commands.ControllerCommand;
import com.objectica.bbot.engine.service.impl.RestEngineController;
import com.objectica.bbot.rumblepad.actions.ButtonAction;
import com.objectica.bbot.rumblepad.actions.ControlAction;
import com.objectica.bbot.rumblepad.actions.JoypadAction;
import com.objectica.bbot.rumblepad.listeners.ControlListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sergey Podgurskiy
 */
public class EngineControlListener implements ControlListener
{
    private static final Logger logger = LoggerFactory.getLogger(EngineControlListener.class);
    private EngineController controller = new RestEngineController("http://bbot:8080/engine-control");
    private boolean echo = true;

    public void actionFired(ControlAction action)
    {
        try
        {
            if (action instanceof JoypadAction)
            {
                JoypadAction joypadAction = (JoypadAction) action;
                int value = Math.abs(joypadAction.getVertical());
                if (value < 10)
                {
                    value = 0;
                }
                if (joypadAction.getVertical() > 0)
                {
                    controller.sendCommand(joypadAction.getNumber() - 1, ControllerCommand.MOVE_FORWARD, (byte) value);
                } else
                {
                    controller.sendCommand(joypadAction.getNumber() - 1, ControllerCommand.MOVE_BACKWARD, (byte) value);
                }
            } else if (action instanceof ButtonAction)
            {
                ButtonAction buttonAction = ((ButtonAction) action);
                // Free run
                switch (buttonAction.getNumber())
                {
                    case 4:
                    {
                        controller.sendCommand(0, ControllerCommand.FREE_RUN, (byte) 0);
                        controller.sendCommand(1, ControllerCommand.FREE_RUN, (byte) 0);
                        break;
                    }
                    case 7:
                    {
                        echo = !echo;
                        logger.info("Device logging(echo) {}", echo ? "enabled" : "disabled");
                        controller.sendCommand(0, ControllerCommand.ECHO, echo ? (byte) 1 : (byte) 0);
                    }
                }
            }
        } catch (Exception e)
        {
            logger.error("Error in engineControlListener", e);
        }
    }
}