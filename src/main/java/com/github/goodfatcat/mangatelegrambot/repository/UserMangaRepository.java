package com.github.goodfatcat.mangatelegrambot.repository;

import com.github.goodfatcat.mangatelegrambot.repository.entity.UserManga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMangaRepository extends JpaRepository<UserManga, Long> {

}
