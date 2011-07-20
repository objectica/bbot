package com.objectica.bbot.engine.service.commands;

import ch.qos.logback.core.encoder.EchoEncoder;

public enum ControllerCommand {
    MOVE_BACKWARD((byte) 0x0),
    MOVE_FORWARD((byte) 0x1),
    FREE_RUN((byte) 0x2),
    ECHO((byte) 0x3);

    private byte command;

    ControllerCommand(byte command) {
        this.command = command;
    }

    public byte getCommand() {
        return command;
    }

    public static ControllerCommand fromValue(byte value) {
        for (ControllerCommand engineCommand : values()) {
            if (engineCommand.getCommand() == value) {
                return engineCommand;
            }
        }
        return null;
    }
}
