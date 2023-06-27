package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.repository.entity.Chapter;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

class ScheduleExecutingServiceImplTest {
    private MangaService mangaService = Mockito.mock(MangaService.class);
    private TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
    private SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
    private ScheduleExecutingService scheduleExecutingService = new ScheduleExecutingServiceImpl(mangaService, sendBotMessageService, telegramUserService);

    @Test
    public void shouldSendUpdatesToUsers() {
        Mockito.when(telegramUserService.retrieveAllActiveUsers()).thenReturn(getActiveUsers());
        Mockito.when(mangaService.updateAllMangaLastChapter(getActiveUsers())).thenReturn(getUpdatedManga());
        Mockito.when(telegramUserService.findAllUsersThatReadManga(getUpdatedManga())).thenReturn(getUsersThatReadManga());

        scheduleExecutingService.sendUpdatesToUsers();

        Mockito.verify(telegramUserService).retrieveAllActiveUsers();
        Mockito.verify(mangaService).updateAllMangaLastChapter(getActiveUsers());
        Mockito.verify(telegramUserService).findAllUsersThatReadManga(getUpdatedManga());
        Mockito.verify(sendBotMessageService, Mockito.times(3)).sendMessage(Mockito.anyString(), Mockito.anyString());
    }



    private List<TelegramUser> getActiveUsers() {
        List<TelegramUser> users = new ArrayList<>();
        users.add(new TelegramUser("123", true, 123));
        users.add(new TelegramUser("124", true, 124));
        users.add(new TelegramUser("125", true, 125));
        users.add(new TelegramUser("126", true, 126));
        return users;
    }

    private List<Manga> getUpdatedManga() {
        List<Manga> updatedManga = new ArrayList<>();
        Manga manga = new Manga();
        manga.setLastChapter(new Chapter(1, 1, "some1"));
        updatedManga.add(manga);
        manga = new Manga();
        manga.setLastChapter(new Chapter(2, 2, "some2"));
        updatedManga.add(manga);
        return updatedManga;
    }

    private Map<TelegramUser, List<Manga>> getUsersThatReadManga() {
        Map<TelegramUser, List<Manga>> usersThatReadManga = new HashMap<>();
        List<TelegramUser> activeUsers = getActiveUsers();

        usersThatReadManga.put(activeUsers.get(0), List.of(getUpdatedManga().get(0)));
        usersThatReadManga.put(activeUsers.get(1), getUpdatedManga());

        return usersThatReadManga;
    }
}