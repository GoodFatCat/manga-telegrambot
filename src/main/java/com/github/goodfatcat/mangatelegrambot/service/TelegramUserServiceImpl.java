package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.repository.TelegramUserRepository;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementation of interface {@link TelegramUserService}.
 */

@Service
public class TelegramUserServiceImpl implements TelegramUserService{

    private final TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserServiceImpl(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    public void save(TelegramUser telegramUser) {
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public List<TelegramUser> retrieveAllActiveUsers() {
        return telegramUserRepository.findAllByActiveTrue();
    }

    @Override
    public Optional<TelegramUser> findByChatId(String chatId) {
        return telegramUserRepository.findById(chatId);
    }

    @Override
    public Map<TelegramUser, List<Manga>> findAllUsersThatReadManga(List<Manga> mangas) {
        Map<TelegramUser, List<Manga>> usersReadMangas = new HashMap<>();

        for (Manga manga : mangas) {
            Set<TelegramUser> usersReadOneManga = telegramUserRepository.findUsersReadManga(manga);
            for (TelegramUser telegramUser : usersReadOneManga) {
                if (usersReadMangas.containsKey(telegramUser)) {
                    usersReadMangas.get(telegramUser).add(manga);
                } else {
                    usersReadMangas.put(telegramUser, new ArrayList<>(List.of(manga)));
                }
            }
        }

        return usersReadMangas;
    }
}
