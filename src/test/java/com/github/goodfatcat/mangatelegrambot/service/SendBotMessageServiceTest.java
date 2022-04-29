package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.bot.MangaTelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@DisplayName("Unit-level testing for SendBotMessageService")
public class SendBotMessageServiceTest {
    private MangaTelegramBot mangaTelegramBot;
    private SendBotMessageService sendBotMessageService;

    @BeforeEach
    public void init() {
        mangaTelegramBot = Mockito.mock(MangaTelegramBot.class);
        sendBotMessageService = new SendBotMessageServiceImpl(mangaTelegramBot);
    }

    @Test
    public void ShouldProperlySendMessage() throws TelegramApiException {
        // given
        String chatId = "test_chat_id";
        String message = "test_message";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.enableHtml(true);

        // when
        sendBotMessageService.sendMessage(chatId, message);

        //then
        Mockito.verify(mangaTelegramBot).execute(sendMessage);
    }

}
