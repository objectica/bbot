package com.objectica.bbot.rumblepad.actions;

public class JoypadAction implements ControlAction {
    private int number;
    private int vertical;
    private int horizontal;

    public JoypadAction(int number, int vertical, int horizontal) {
        this.number = number;
        this.vertical = vertical;
        this.horizontal = horizontal;
    }

    public int getNumber() {
        return number;
    }

    public int getVertical() {
        return vertical;
    }

    public int getHorizontal() {
        return horizontal;
    }

    @Override
    public String toString() {
        return "JoypadAction{" +
                "number=" + number +
                ", vertical=" + vertical +
                ", horizontal=" + horizontal +
                '}';
    }
}
