package com.github.goodfatcat.mangatelegrambot.repository.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserMangaId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tgUserId;
    private long mangaId;
}
