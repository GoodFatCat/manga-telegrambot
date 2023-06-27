package com.github.goodfatcat.mangatelegrambot.repository.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

/**
 * Telegram user entity.
 */

@Data
@Entity
@Table(name = "tg_user")
public class TelegramUser {

    @Id
    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "active")
    private boolean active;

    @Column(name = "mangalib_id")
    private long mangalibId;

    @ManyToMany(mappedBy = "readers")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Manga> readableManga;

    public TelegramUser(String chatId, boolean active, long mangalibId) {
        this.chatId = chatId;
        this.active = active;
        this.mangalibId = mangalibId;
    }

    public TelegramUser() {
    }
}
