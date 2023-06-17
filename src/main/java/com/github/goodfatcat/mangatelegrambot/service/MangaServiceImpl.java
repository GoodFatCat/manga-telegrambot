package com.github.goodfatcat.mangatelegrambot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.goodfatcat.mangatelegrambot.exception.NoSuchUserException;
import com.github.goodfatcat.mangatelegrambot.model.Items;
import com.github.goodfatcat.mangatelegrambot.repository.MangaRepository;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Implementation of {@link MangaService} interface.
 */
@Service
public class MangaServiceImpl implements MangaService {
    private MangaRepository mangaRepository;
    private WebClient webClient;
    private ObjectMapper objectMapper;

    @Autowired
    public MangaServiceImpl(MangaRepository mangaRepository, WebClient webClient, ObjectMapper objectMapper) {
        this.mangaRepository = mangaRepository;
        this.webClient = webClient;
        this.objectMapper = objectMapper;
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

    @Override
    public void updateAllMangaLastChapterAt() {
        List<Manga> allManga = getAllManga();

        for (Manga manga : allManga) {
            String HTML = getHtml(manga);
            assert HTML != null;

            Document doc = Jsoup.parse(HTML);
            Elements scripts = doc.getElementsByTag("script");

            updateDateTime(manga, scripts);
        }

        saveAll(allManga);
    }

    private String getHtml(Manga manga) {
        String slug = manga.getSlug();

        return webClient.get()
                .uri(slug)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
    }

    private void updateDateTime(Manga manga, Elements scripts) {
        for (Element script : scripts) {
            String scriptValue = script.data().trim();
            if (scriptValue.contains("window.__DATA__")) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String json = scriptValue.split("window.__DATA__ = ")[1];
                try {
                    JsonNode tree = objectMapper.readTree(json);
                    String chapterCreatedAt = tree.get("chapters")
                            .get("list")
                            .get(0)
                            .get("chapter_created_at")
                            .toString()
                            .replace("\"", "");

                    LocalDateTime newDateTime = LocalDateTime.parse(chapterCreatedAt, dateTimeFormatter);
                    manga.setLastChapterAt(newDateTime);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
