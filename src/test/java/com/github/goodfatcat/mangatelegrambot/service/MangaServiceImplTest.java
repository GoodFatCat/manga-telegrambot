package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.exception.NoSuchUserException;
import com.github.goodfatcat.mangatelegrambot.DTO.Items;
import com.github.goodfatcat.mangatelegrambot.DTO.JsonManga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Chapter;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.*;

@ActiveProfiles(value = "test")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MangaServiceImplTest {

    @Autowired
    MangaService mangaService;
    @Autowired
    TelegramUserService telegramUserService;

    @Test
    public void shouldGetBookmarkFromWeb() {
        String userId = "2098855";

        Items items = mangaService.getMangasByMangalibId(Integer.parseInt(userId));

        Assertions.assertNotNull(items);
        Assertions.assertFalse(items.getMangaSet().isEmpty());
    }

    @Test
    public void shouldThrowNoSuchUserException() {
        int userMangalibId = 9999999;
        Assertions.assertThrows(NoSuchUserException.class, () -> mangaService.getMangasByMangalibId(userMangalibId));
    }

    @Test
    @Sql(scripts = {"/sql/clearDB.sql"})
    public void saveAllManga() {
        //init
        List<Manga> expectedData = initData();

        mangaService.saveAll(expectedData);

        List<Manga> actualData = mangaService.getAllMangas();
        Assertions.assertEquals(expectedData, actualData);
    }

    private static List<Manga> initData() {
        JsonManga jsonManga = new JsonManga();
        jsonManga.setId(39);
        jsonManga.setCover("QPXzTLH6zrIw");
        jsonManga.setStatus(2);
        jsonManga.setRusName("Берсерк");
        jsonManga.setLastChapterAt(LocalDateTime.of(1999, 8, 23, 8, 23, 39));
        jsonManga.setSlug("berserk");

        Chapter chapter1 = new Chapter();
        chapter1.setName("Стена");
        chapter1.setNumber(359);
        chapter1.setVolume(41);

        jsonManga.setLastChapter(chapter1);

        Manga manga1 = Manga.getInstanceFromJsonManga(jsonManga);

        jsonManga.setId(442);
        jsonManga.setCover("CvABU1zFzds8");
        jsonManga.setStatus(2);
        jsonManga.setRusName("Получеловек");
        jsonManga.setLastChapterAt(LocalDateTime.of(1999, 2, 12, 14, 30, 40));
        jsonManga.setSlug("ajin");

        Chapter chapter2 = new Chapter();
        chapter2.setName("Пока жизнь идёт своим чередом");
        chapter2.setVolume(18);
        chapter2.setNumber(86);

        jsonManga.setLastChapter(chapter2);

        Manga manga2 = Manga.getInstanceFromJsonManga(jsonManga);

        List<Manga> expectedData = new ArrayList<>();
        expectedData.add(manga1);
        expectedData.add(manga2);
        return expectedData;
    }
}
