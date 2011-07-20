package com.objectica.bbot.rumblepad.actions;

public class ButtonAction implements ControlAction {
    private final int number;

    public ButtonAction(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ButtonAction that = (ButtonAction) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public String toString() {
        return "ButtonAction{" +
                "number=" + number +
                '}';
    }
}
