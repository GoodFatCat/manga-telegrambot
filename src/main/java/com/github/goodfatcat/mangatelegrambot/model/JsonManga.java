package com.github.goodfatcat.mangatelegrambot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Chapter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JsonManga {
    @JsonProperty(value = "manga_id")
    private long id;
    @JsonProperty(value = "rus_name")
    private String rusName;
    private String slug;
    private String cover;
    @JsonProperty(value = "last_chapter_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastChapterAt;
    @JsonProperty(value = "last_chapter")
    private Chapter lastChapter;
    private int status;
}
