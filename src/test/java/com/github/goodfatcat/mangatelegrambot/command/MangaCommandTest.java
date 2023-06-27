package com.github.goodfatcat.mangatelegrambot.command;

import com.github.goodfatcat.mangatelegrambot.exception.NoSuchUserException;
import com.github.goodfatcat.mangatelegrambot.DTO.Items;
import com.github.goodfatcat.mangatelegrambot.DTO.JsonManga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Chapter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.Set;

import static com.github.goodfatcat.mangatelegrambot.command.MangaCommand.*;

class MangaCommandTest extends AbstractCommandTest{
    @Override
    String getCommandName() {
        return CommandName.MANGA.getCommandName();
    }

    String getCommandName(String mangalibLink) {
        return getCommandName() + " " + mangalibLink;
    }

    @Override
    String getCommandMessage() {
        return MangaCommand.SUCCESS_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new MangaCommand(sendBotMessageService, mangaService, telegramUserService);
    }

    @Override
    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        //given
        Long chatId = 1234567824356L;

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName("777"));
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(getCommandMessage());
        sendMessage.enableHtml(true);

        JsonManga jsonManga = new JsonManga();
        jsonManga.setId(1);
        jsonManga.setLastChapter(new Chapter(1, 3, "стена"));
        jsonManga.setSlug("berserk");
        jsonManga.setCover("dgotr");
        jsonManga.setRusName("Берсерк");
        jsonManga.setStatus(1);
        jsonManga.setLastChapterAt(LocalDateTime.now());
        Set<JsonManga> jsonMangaSet = Set.of(jsonManga);

        Items items = new Items();
        items.setMangaSet(jsonMangaSet);

        Mockito.when(mangaService.getMangasByMangalibId(777)).thenReturn(items);

        getCommand().execute(update);

        Mockito.verify(telegramUserService).findByChatId(chatId.toString());
        Mockito.verify(telegramUserService).save(Mockito.any());
        Mockito.verify(mangaTelegramBot).execute(sendMessage);
    }

    @Test
    public void shouldSendErrorNoLinkMessage() throws TelegramApiException {
        //given
        Long chatId = 1234567824356L;

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName(""));
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(ERROR_NO_LINK_MESSAGE);
        sendMessage.enableHtml(true);

        getCommand().execute(update);

        Mockito.verify(mangaTelegramBot).execute(sendMessage);
    }

    @Test
    public void shouldSendErrorLinkMessage() throws TelegramApiException{
        Long chatId = 1234567824356L;

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName("gf"));
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(ERROR_LINK_MESSAGE);
        sendMessage.enableHtml(true);

        getCommand().execute(update);

        Mockito.verify(mangaTelegramBot).execute(sendMessage);
    }

    @Test
    public void shouldSendNoUserErrorMessage() throws TelegramApiException {
        Long chatId = 1234567824356L;
        int mangalibId = 9999999;

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName(Integer.toString(mangalibId)));
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(ERROR_NO_USER_MESSAGE);
        sendMessage.enableHtml(true);

        Mockito.when(mangaService.getMangasByMangalibId(mangalibId)).thenThrow(NoSuchUserException.class);

        getCommand().execute(update);

        Mockito.verify(mangaTelegramBot).execute(sendMessage);
    }
}