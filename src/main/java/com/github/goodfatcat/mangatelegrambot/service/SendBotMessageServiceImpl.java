package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.bot.MangaTelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Implementation of {@link SendBotMessageService} interface.
 */

@Service
@Slf4j
public class SendBotMessageServiceImpl implements SendBotMessageService {

    private final MangaTelegramBot mangaTelegramBot;

    @Autowired
    public SendBotMessageServiceImpl(MangaTelegramBot mangaTelegramBot) {
        this.mangaTelegramBot = mangaTelegramBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try {
            mangaTelegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.toString());
        }
    }
}
