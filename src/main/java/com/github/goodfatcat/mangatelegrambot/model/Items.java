package com.github.goodfatcat.mangatelegrambot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

/**
 * Object for parsing json from webSite
 */
@Data
public class Items {
    @JsonProperty(value = "items")
    private Set<JsonManga> mangaSet;
}
