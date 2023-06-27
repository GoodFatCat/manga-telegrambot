package com.github.goodfatcat.mangatelegrambot.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.UserManga;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Object for parsing json from webSite
 */
@Data
public class Items {
    @JsonProperty(value = "items")
    private Set<JsonManga> mangaSet;

    public static List<Manga> getMangaList(Items items) {
        return items.getMangaSet().stream()
                .map(Manga::getInstanceFromJsonManga)
                .collect(Collectors.toList());
    }

    public static List<UserManga> getUserMangaList(String telegramUserId, Items items) {
        return items.getMangaSet()
                .stream()
                .map(manga -> new UserManga(manga.getId(), telegramUserId, manga.getStatus()))
                .collect(Collectors.toList());
    }
}
