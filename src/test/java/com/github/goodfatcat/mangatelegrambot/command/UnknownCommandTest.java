package com.github.goodfatcat.mangatelegrambot.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.goodfatcat.mangatelegrambot.command.UnknownCommand.UNKNOWN_MESSAGE;

@DisplayName("Unit-level testing for UnknownCommand")
public class UnknownCommandTest extends AbstractCommandTest{
    @Override
    String getCommandName() {
        return "/setdfgrt";
    }

    @Override
    String getCommandMessage() {
        return UNKNOWN_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UnknownCommand(sendBotMessageService);
    }
}
