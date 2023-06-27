package com.github.goodfatcat.mangatelegrambot.service;


import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@link org.springframework.stereotype.Service} for handling {@link TelegramUser} entity.
 */

public interface TelegramUserService {

    /**
     * Save provided {@link TelegramUser} entity.
     *
     * @param telegramUser provided telegram user.
     */
    void save(TelegramUser telegramUser);

    /**
     * Retrieve all active {@link TelegramUser}.
     *
     * @return the collection of the active {@link TelegramUser} objects.
     */
    List<TelegramUser> retrieveAllActiveUsers();

    /**
     * Find a {@link TelegramUser} by chatId.
     *
     * @param chatId provided ChatId.
     * @return {@link TelegramUser} with provided chat ID or null otherwise.
     */

    Optional<TelegramUser> findByChatId(String chatId);

    Map<TelegramUser, List<Manga>> findAllUsersThatReadManga(List<Manga> mangas);
}
