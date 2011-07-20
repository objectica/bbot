package com.objectica.bbot.rumblepad;

import com.objectica.bbot.rumblepad.actions.ControlAction;


public class ControlListener {

    public void actionFired(ControlAction action) {
        System.out.println("ActionFired: " + action);
    }
}
