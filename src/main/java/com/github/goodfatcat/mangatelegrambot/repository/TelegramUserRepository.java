package com.github.goodfatcat.mangatelegrambot.repository;

import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * {@link Repository} for handling with {@link TelegramUser} entity.
 */

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, String> {
    List<TelegramUser> findAllByActiveTrueAndMangalibIdNot(long id);

    @Query("SELECT user FROM TelegramUser user " +
            "JOIN UserManga um ON user.chatId = um.tgUserId " +
            "WHERE um.mangaId = :#{#manga.getId()} AND user.active = true AND um.status = 1")
    Set<TelegramUser> findUsersReadManga(@Param("manga") Manga manga);
}
