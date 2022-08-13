package com.github.goodfatcat.mangatelegrambot.command;

import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import com.github.goodfatcat.mangatelegrambot.service.SendBotMessageService;
import com.github.goodfatcat.mangatelegrambot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * start {@link Command}
 */

public class StartCommand implements Command {

    private SendBotMessageService sendBotMessageService;
    private TelegramUserService telegramUserService;

    public final static String START_MESSAGE = "Привет. Я Mangalib Telegram Bot. Я помогу тебе быть в курсе обновлений " +
            "той манги, которая тебе интересна. Я еще маленький и только учусь.";

    public StartCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();

        telegramUserService.findByChatId(chatId).ifPresentOrElse(
                user -> {
                    user.setActive(true);
                    telegramUserService.save(user);
                },
                () -> {
                    TelegramUser telegramUser = new TelegramUser();
                    telegramUser.setChatId(chatId);
                    telegramUser.setActive(true);
                    telegramUserService.save(telegramUser);
                }
        );

        sendBotMessageService.sendMessage(chatId, START_MESSAGE);
    }
}
