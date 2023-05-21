package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.repository.UserMangaRepository;
import com.github.goodfatcat.mangatelegrambot.repository.entity.UserManga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMangaServiceImpl implements UserMangaService {
    private UserMangaRepository userMangaRepository;

    @Autowired
    public UserMangaServiceImpl(UserMangaRepository userMangaRepository) {
        this.userMangaRepository = userMangaRepository;
    }

    @Override
    public void save(UserManga userManga) {
        userMangaRepository.save(userManga);
    }

    @Override
    public void saveAll(List<UserManga> userMangaList) {
        userMangaRepository.saveAll(userMangaList);
    }
}
