package com.objectica.bbot.behavior.impl;

import com.objectica.bbot.behavior.Behavior;
import com.objectica.bbot.behavior.BehaviorContext;
import com.objectica.bbot.triggers.impl.RemoteCommand;

/**
 * @author curly
 */
public class ConsoleBehavior implements Behavior {

    @Override
    public void triggerFired(BehaviorContext context) {
        // TODO implement arbitrage
        RemoteCommand command = (RemoteCommand)context.getDataMap().get("command");
        System.err.println(command.getValue());
    }
}
