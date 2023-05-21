package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.repository.entity.UserManga;

import java.util.List;

public interface UserMangaService {
    void save(UserManga userManga);
    void saveAll(List<UserManga> userMangaList);
}
