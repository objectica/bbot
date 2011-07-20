package com.objectica.bbot.rumblepad.listeners;

import com.objectica.bbot.rumblepad.actions.ControlAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingControlListener implements ControlListener {
    private static final Logger logger = LoggerFactory.getLogger(LoggingControlListener.class);

    public void actionFired(ControlAction action) {
        logger.info(action.toString());
    }
}
