package com.github.goodfatcat.mangatelegrambot.command;

import com.github.goodfatcat.mangatelegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

/*
 * start {@link Command}
 */

public class StartCommand implements Command {

    private SendBotMessageService sendBotMessageService;

    public final static String START_MESSAGE = "Привет. Я Mangalib Telegram Bot. Я помогу тебе быть в курсе обновлений " +
            "той манги, которая тебе интересна. Я еще маленький и только учусь.";

    public StartCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
    }
}
