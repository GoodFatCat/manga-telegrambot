package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.repository.TelegramUserRepository;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TelegramUserServiceTest {
    private TelegramUserRepository telegramUserRepository = Mockito.mock(TelegramUserRepository.class);
    private TelegramUserService telegramUserService = new TelegramUserServiceImpl(telegramUserRepository);

    @Test
    public void shouldProperlyFindAllUsersThatReadManga() {
        List<Manga> mangaList = new ArrayList<>();
        Manga manga = new Manga();
        manga.setId(1);
        mangaList.add(manga);
        manga = new Manga();
        manga.setId(2);
        mangaList.add(manga);
        manga = new Manga();
        manga.setId(3);
        mangaList.add(manga);

        Mockito.when(telegramUserRepository.findUsersReadManga(mangaList.get(0))).thenReturn(Set.of(getUsers().get(0)));
        Mockito.when(telegramUserRepository
                .findUsersReadManga(mangaList.get(1)))
                .thenReturn(Set.of(getUsers().get(1), getUsers().get(0)));
        Mockito.when(telegramUserRepository.findUsersReadManga(mangaList.get(2))).thenReturn(Set.of(getUsers().get(2)));

        Map<TelegramUser, List<Manga>> allUsersThatReadManga = telegramUserService.findAllUsersThatReadManga(mangaList);

        Mockito.verify(telegramUserRepository, Mockito.times(3)).findUsersReadManga(Mockito.any());
        assertEquals(3, allUsersThatReadManga.size());
    }

    private List<TelegramUser> getUsers() {
        List<TelegramUser> users = new ArrayList<>();
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId("123");
        users.add(telegramUser);
        telegramUser = new TelegramUser();
        telegramUser.setChatId("124");
        users.add(telegramUser);
        telegramUser = new TelegramUser();
        telegramUser.setChatId("125");
        users.add(telegramUser);
        return users;
    }
}