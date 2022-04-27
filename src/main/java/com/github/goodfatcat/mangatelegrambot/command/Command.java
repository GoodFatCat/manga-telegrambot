package com.github.goodfatcat.mangatelegrambot.command;

import org.telegram.telegrambots.meta.api.objects.Update;

/*
 * Command interface for handling telegram-bot commands.
 */

public interface Command {

    /*
     * Main method which executes command logic
     */

    void execute(Update update);
}
