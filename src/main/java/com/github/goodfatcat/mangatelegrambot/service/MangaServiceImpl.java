package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.exception.NoSuchUserException;
import com.github.goodfatcat.mangatelegrambot.DTO.Items;
import com.github.goodfatcat.mangatelegrambot.repository.MangaRepository;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import com.github.goodfatcat.mangatelegrambot.repository.entity.UserManga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.*;

/**
 * Implementation of {@link MangaService} interface.
 */
@Service
@Slf4j
public class MangaServiceImpl implements MangaService {
    private MangaRepository mangaRepository;
    private WebClient webClient;
    private UserMangaService userMangaService;

    @Autowired
    public MangaServiceImpl(MangaRepository mangaRepository, WebClient webClient, UserMangaService userMangaService) {
        this.mangaRepository = mangaRepository;
        this.webClient = webClient;
        this.userMangaService = userMangaService;
    }

    @Override
    public Items getMangasByMangalibId(long id) {
        Items jsonWrap = null;
        String errorMessage;
        try {
            errorMessage = webClient
                    .get()
                    .uri("bookmark/" + id)
                    .retrieve()
                    .onStatus(HttpStatus.FORBIDDEN::equals,
                            clientResponse -> clientResponse.bodyToMono(String.class).map(RuntimeException::new))
                    .bodyToMono(String.class)
                    .block();
            log.error(errorMessage);
            throw new Exception();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//
//        if (jsonWrap == null || jsonWrap.getMangaSet().isEmpty()) {
//            throw new NoSuchUserException("No such user or user has not any bookmark");
//        }
//        return jsonWrap;
    }

    @Override
    public void saveAll(List<Manga> mangas) {
        if (mangas == null) {
            throw new RuntimeException("Mangas must be not null");
        }
        mangaRepository.saveAll(mangas);
    }

    @Override
    public Manga getManga(long id) {
        return mangaRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Manga> getAllMangas() {
        return mangaRepository.findAll();
    }

    @Override
    public List<Manga> updateAllMangaLastChapter(List<TelegramUser> activeUsers) {
        Set<Manga> uniqMangas = new HashSet<>();
        List<Manga> updatedMangas = new ArrayList<>();
        Map<String, List<UserManga>> userMangaMap = new HashMap<>();

        getUniqMangasAndUserMangaMap(activeUsers, uniqMangas, userMangaMap);
        updateOrSaveManga(uniqMangas, updatedMangas);

        for (String activeTelegramUserId : userMangaMap.keySet()) {
            userMangaService.saveAll(userMangaMap.get(activeTelegramUserId));
        }
        return updatedMangas;
    }

    private void getUniqMangasAndUserMangaMap(List<TelegramUser> activeUsers, Set<Manga> uniqMangas, Map<String, List<UserManga>> userMangaMap) {
        for (TelegramUser activeUser : activeUsers) {
            Items items = getMangasByMangalibId(activeUser.getMangalibId());
            List<Manga> mangaList = Items.getMangaList(items);
            userMangaMap.put(activeUser.getChatId(), Items.getUserMangaList(activeUser.getChatId(), items));
            uniqMangas.addAll(mangaList);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateOrSaveManga(Set<Manga> uniqMangas, List<Manga> updatedMangas) {
        for (Manga manga : uniqMangas) {
            if (mangaRepository.existsById(manga.getId())) {
                Manga oldManga = getManga(manga.getId());
                if (manga.getLastChapterAt().isAfter(oldManga.getLastChapterAt())) {
                    mangaRepository.save(manga);
                    updatedMangas.add(manga);
                }
            } else {
                mangaRepository.save(manga);
            }
        }
    }
}
