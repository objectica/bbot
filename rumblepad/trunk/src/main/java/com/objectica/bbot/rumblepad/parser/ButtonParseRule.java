package com.objectica.bbot.rumblepad.parser;

import com.objectica.bbot.rumblepad.actions.ButtonAction;
import com.objectica.bbot.rumblepad.actions.ControlAction;

public class ButtonParseRule implements ActionParseRule {
    private int buttonNumber;
    private int byteIndex;
    private int mask;

    public ButtonParseRule(int buttonNumber, int byteIndex, int mask) {
        this.buttonNumber = buttonNumber;
        this.byteIndex = byteIndex;
        this.mask = mask;
    }

    public ControlAction parseAction(byte[] data) {
        if (data.length > byteIndex) {
            if ((data[byteIndex] & mask) > 0) {
                return new ButtonAction(buttonNumber);
            }
        }
        return null;
    }
}
