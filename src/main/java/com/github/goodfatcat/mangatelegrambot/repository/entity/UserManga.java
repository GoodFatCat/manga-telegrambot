package com.github.goodfatcat.mangatelegrambot.repository.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_manga")
public class UserManga {
    // todo change id field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "manga_id")
    private long mangaId;
    @Column(name = "user_id")
    private String userId;

    private ReadingStatus status;

    public UserManga() {
    }

    public UserManga(long mangaId, String userId, int status) {
        this.mangaId = mangaId;
        this.userId = userId;
        this.status = ReadingStatus.findByStatusCode(status);
    }
}
