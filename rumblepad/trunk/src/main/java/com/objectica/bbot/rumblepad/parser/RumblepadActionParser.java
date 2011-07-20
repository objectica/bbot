package com.objectica.bbot.rumblepad.parser;

import com.objectica.bbot.rumblepad.actions.ControlAction;

import java.util.*;

public class RumblepadActionParser {
    private static Set<ActionParseRule> parseRules = new LinkedHashSet<ActionParseRule>();

    public RumblepadActionParser() {
        parseRules.add(new ButtonParseRule(1, 4, 0x10));
        parseRules.add(new ButtonParseRule(2, 4, 0x20));
        parseRules.add(new ButtonParseRule(3, 4, 0x40));
        parseRules.add(new ButtonParseRule(4, 4, 0x80));
        parseRules.add(new ButtonParseRule(5, 5, 0x01));
        parseRules.add(new ButtonParseRule(6, 5, 0x02));
        parseRules.add(new ButtonParseRule(7, 5, 0x04));
        parseRules.add(new ButtonParseRule(8, 5, 0x08));
        parseRules.add(new ButtonParseRule(9, 5, 0x10));
        parseRules.add(new ButtonParseRule(10, 5, 0x20));
        parseRules.add(new JoypadParseRule(1, 2, 3));
        parseRules.add(new JoypadParseRule(2, 0, 1));
    }

    public Collection<ControlAction> parseActions(byte[] data) {
        Collection<ControlAction> result = new ArrayList<ControlAction>();

        for (ActionParseRule parseRule : parseRules) {
            ControlAction action = parseRule.parseAction(data);
            if (action != null) {
                result.add(action);
            }
        }
        return result;
    }
}
