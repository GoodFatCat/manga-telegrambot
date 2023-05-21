package com.github.goodfatcat.mangatelegrambot.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * Chapter is embeddable class for {@link Manga} entity
 */
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {
    private double number;
    private int volume;
    private String name;
}