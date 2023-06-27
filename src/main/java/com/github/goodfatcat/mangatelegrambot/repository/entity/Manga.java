package com.github.goodfatcat.mangatelegrambot.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.goodfatcat.mangatelegrambot.DTO.JsonManga;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Manga entity
 */
@Data
@Entity
public class Manga {
    @Id
    @JsonProperty(value = "manga_id")
    private long id;
    @JsonProperty(value = "rus_name")
    @Column(name = "rus_name")
    private String rusName;
    private String slug;
    private String cover;
    @JsonProperty(value = "last_chapter_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastChapterAt;
    @JsonProperty(value = "last_chapter")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "number", column = @Column(name = "last_chapter_number")),
            @AttributeOverride(name = "volume", column = @Column(name = "last_chapter_volume")),
            @AttributeOverride(name = "name", column = @Column(name = "last_chapter_name"))
    })
    private Chapter lastChapter;
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_manga",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "tg_user_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TelegramUser> readers;

    public Manga() {
    }

    private Manga(JsonManga jsonManga) {
        this.id = jsonManga.getId();
        this.rusName = jsonManga.getRusName();
        this.lastChapterAt = jsonManga.getLastChapterAt();
        this.lastChapter = jsonManga.getLastChapter();
        this.cover = jsonManga.getCover();
        this.slug = jsonManga.getSlug();
    }

    public static Manga getInstanceFromJsonManga(JsonManga jsonManga) {
        return new Manga(jsonManga);
    }
}
