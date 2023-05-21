package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.exception.NoSuchUserException;
import com.github.goodfatcat.mangatelegrambot.model.Items;
import com.github.goodfatcat.mangatelegrambot.repository.MangaRepository;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Implementation of {@link MangaService} interface.
 */
@Service
public class MangaServiceImpl implements MangaService {
    private MangaRepository mangaRepository;
    private WebClient webClient;

    @Autowired
    public MangaServiceImpl(MangaRepository mangaRepository, WebClient webClient) {
        this.mangaRepository = mangaRepository;
        this.webClient = webClient;
    }

    public Items getBookmarkFromWeb(int id) {
        Items jsonWrap = webClient
                .get()
                .uri("bookmark/" + id)
                .retrieve()
                .bodyToMono(Items.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();

        if (jsonWrap == null || jsonWrap.getMangaSet().isEmpty()) {
            throw new NoSuchUserException("No such user or user has not any bookmark");
        }
        return jsonWrap;
    }

    @Override
    public void saveAll(List<Manga> mangas) {
        if (mangas == null) {
            throw new RuntimeException();
        }
        mangaRepository.saveAll(mangas);
    }

    @Override
    public Manga getManga(long id) {
        return mangaRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Manga> getAllManga() {
        return mangaRepository.findAll();
    }
}
