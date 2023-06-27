package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.DTO.Items;
import com.github.goodfatcat.mangatelegrambot.DTO.JsonManga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Chapter;

import java.util.List;

/**
 * Service for working with manga
 */
public interface MangaService {
    /**
     * This method return wrap {@link Items} of {@link JsonManga} from mangaLib.me/bookmark/{id}
     *
     * @param id provided user id in mangalib.me
     * @return {@link Items} model for parse from json
     */
    Items getMangasByMangalibId(long id);

    /**
     * This method save manga in database
     *
     * @param mangas provided {@link Manga} that need to save
     */
    void saveAll(List<Manga> mangas);

    /**
     * This method find {@link Manga} from database
     *
     * @param id provided id that need to find
     * @return {@link Manga}
     */
    Manga getManga(long id);

    /**
     * This method find all {@link Manga} from database
     *
     * @return List of {@link Manga}
     */
    List<Manga> getAllMangas();

    /**
     * This method update property {@link Chapter} lastChapter and lastChapterAt in all {@link Manga}
     *
     * @return List updated {@link Manga}
     */
    List<Manga> updateAllMangaLastChapter(List<TelegramUser> activeUsers);
}
