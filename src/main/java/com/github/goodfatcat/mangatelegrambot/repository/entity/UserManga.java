package com.github.goodfatcat.mangatelegrambot.repository.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_manga")
@IdClass(value = UserMangaId.class)
public class UserManga {
    @Column(name = "manga_id")
    @Id
    private long mangaId;
    @Column(name = "tg_user_id")
    @Id
    private String tgUserId;

    private ReadingStatus status;

    public UserManga() {
    }

    public UserManga(long mangaId, String tgUserId, int status) {
        this.mangaId = mangaId;
        this.tgUserId = tgUserId;
        this.status = ReadingStatus.findByStatusCode(status);
    }
}
