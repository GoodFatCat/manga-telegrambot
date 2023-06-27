package com.github.goodfatcat.mangatelegrambot.repository;

import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class TelegramUserRepositoryTest {
    @Autowired
    private TelegramUserRepository telegramUserRepository;
    @Autowired
    private MangaRepository mangaRepository;

    @Test
    @Sql({"/sql/clearDB.sql", "/sql/mangas.sql", "/sql/telegram_users.sql", "/sql/user_manga.sql"})
    public void shouldProperlyFindUsersReadManga() {
        List<Manga> mangas = mangaRepository.findAll();
        Manga berserk39 = mangas.get(0);

        Set<TelegramUser> usersReadManga = telegramUserRepository.findUsersReadManga(berserk39);

        assertEquals(2, usersReadManga.size());
    }

}