package com.objectica.bbot.missile;

public enum MissileCommand {
    STOP(0),
    LEFT(1),
    RIGHT(2),
    UP(4),
    DOWN(8),
    FIRE(16);

    private int command;

    MissileCommand(int command) {
        this.command = command;
    }

    public int getCommand() {
        return command;
    }
}
