package com.github.goodfatcat.mangatelegrambot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {
    @Bean
    public WebClient webClient() {
        String baseUrl = "https://mangalib.org/";
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
