package com.github.goodfatcat.mangatelegrambot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public WebClient webClient() {
        String baseUrl = "https://mangalib.me/";
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
