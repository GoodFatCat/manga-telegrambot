package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.model.Items;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.model.JsonManga;

import java.util.List;

/**
 * Service for working with manga
 */
public interface MangaService {
    /**
     * This method get wrap {@link Items} of {@link JsonManga} from mangaLib.me/bookmark/{id}
     *
     * @param id provided user id in mangaLib.me
     * @return {@link Items} model for parse from json
     */
    Items getBookmarkFromWeb(int id);

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
     * @return {@link Manga}
     */
    List<Manga> getAllManga();

    /**
     * This method update property lastChapterAt in all {@link Manga}
     */
    void updateAllMangaLastChapterAt();
}
