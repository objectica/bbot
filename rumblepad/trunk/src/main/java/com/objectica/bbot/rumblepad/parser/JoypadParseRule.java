package com.objectica.bbot.rumblepad.parser;

import com.objectica.bbot.rumblepad.actions.ControlAction;
import com.objectica.bbot.rumblepad.actions.JoypadAction;

public class JoypadParseRule implements ActionParseRule {
    private int joypadNumber;
    private int verticalByteNumber;
    private int horizontalByteNumber;

    public JoypadParseRule(int joypadNumber, int horizontalByteNumber, int verticalByteNumber) {
        this.joypadNumber = joypadNumber;
        this.verticalByteNumber = verticalByteNumber;
        this.horizontalByteNumber = horizontalByteNumber;
    }

    public ControlAction parseAction(byte[] data) {
        int vertical = (data[verticalByteNumber] >= 0 ? 128 : -128) - data[verticalByteNumber];
        int horizontal = (data[horizontalByteNumber] >= 0 ? 128 : -128) - data[horizontalByteNumber];
        if (vertical == 0 && horizontal == 0) {
            return null;
        }
        return new JoypadAction(joypadNumber, vertical, horizontal * -1);
    }
}
