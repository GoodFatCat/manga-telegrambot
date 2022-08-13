package com.github.goodfatcat.mangatelegrambot.command;

public enum CommandName {

    START("/start"),
    STOP("/stop"),
    NO("nocommand"),
    HELP("/help"),
    STAT("/stat");

    private String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
