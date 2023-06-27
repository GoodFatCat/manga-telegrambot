package com.github.goodfatcat.mangatelegrambot.repository;


import com.github.goodfatcat.mangatelegrambot.repository.entity.Chapter;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * Integration-level testing for {@link TelegramUserRepository}.
 */
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class TelegramUserRepositoryIT {

    @Autowired
    private TelegramUserRepository telegramUserRepository;

    @Sql(scripts = {"/sql/clearDB.sql", "/sql/telegram_users.sql"})
    @Test
    public void shouldProperlyFindAllActiveUsers() {
        //when
        List<TelegramUser> users = telegramUserRepository.findAllByActiveTrue();

        //then
        assertEquals(5, users.size());
    }

    @Test
    @Sql(scripts = {"/sql/clearDB.sql","/sql/mangas.sql", "/sql/telegram_users.sql", "/sql/user_manga.sql"})
    public void getUserMangaList() {
        Optional<TelegramUser> user = telegramUserRepository.findById("123456789");
        TelegramUser telegramUser = user.orElseThrow();
        assertEquals(3, telegramUser.getReadableManga().size());
    }

    @Test
    public void shouldProperlySaveTelegramUser() {
        //given
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId("1234567890");
        telegramUser.setActive(false);
        telegramUserRepository.save(telegramUser);

        //when
        Optional<TelegramUser> saved = telegramUserRepository.findById(telegramUser.getChatId());

        //then
        Assertions.assertTrue(saved.isPresent());
        assertEquals(telegramUser, saved.get());
    }

    @Test
    @Sql(scripts = {"/sql/clearDB.sql","/sql/mangas.sql", "/sql/telegram_users.sql", "/sql/user_manga.sql"})
    public void shouldFindUsersByMangas() {
        Manga manga = new Manga();
        manga.setId(39);
        manga.setCover("QPXzTLH6zrIw");
        manga.setLastChapterAt(LocalDateTime.of(2019, 8, 23, 8, 23, 39));
        manga.setRusName("Берсерк");
        manga.setSlug("berserk");
        Chapter chapter = new Chapter();
        chapter.setNumber(359);
        chapter.setVolume(41);
        chapter.setName("Стена");
        manga.setLastChapter(chapter);

        Set<TelegramUser> usersReadManga = telegramUserRepository.findUsersReadManga(manga);

        assertEquals(2, usersReadManga.size());
    }
}
