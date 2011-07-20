package com.objectica.bbot.rumblepad.parser;

import com.objectica.bbot.rumblepad.actions.ControlAction;

public interface ActionParseRule {

    public ControlAction parseAction(byte[] data);
}
