package com.github.goodfatcat.mangatelegrambot.repository;

import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * {@link Repository} for handling with {@link Manga} entity.
 */
public interface MangaRepository extends JpaRepository<Manga, Long> {
}
