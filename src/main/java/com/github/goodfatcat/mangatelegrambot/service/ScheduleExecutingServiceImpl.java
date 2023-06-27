package com.github.goodfatcat.mangatelegrambot.service;

import com.github.goodfatcat.mangatelegrambot.repository.entity.Chapter;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ScheduleExecutingServiceImpl implements ScheduleExecutingService {
    private MangaService mangaService;
    private SendBotMessageService messageService;
    private TelegramUserService telegramUserService;

    @Autowired
    public ScheduleExecutingServiceImpl(MangaService mangaService, SendBotMessageService messageService, TelegramUserService telegramUserService) {
        this.mangaService = mangaService;
        this.messageService = messageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    @Scheduled(fixedDelay = 1000 * 60 * 15, initialDelay = 1000 * 60 * 5)
    public void sendUpdatesToUsers() {
        log.debug("Stating updating manga last chapter");
        List<TelegramUser> activeUsers = telegramUserService.retrieveAllActiveUsers();
        List<Manga> updatedMangas;
        try {
            updatedMangas = mangaService.updateAllMangaLastChapter(activeUsers);
        } catch (RuntimeException e) {
            log.error(e.toString());
            return;
        }

        Map<TelegramUser, List<Manga>> allUsersThatReadManga = telegramUserService.findAllUsersThatReadManga(updatedMangas);

        log.debug(activeUsers.toString());
        log.debug(updatedMangas.toString());
        log.debug(allUsersThatReadManga.toString());

        for (Map.Entry<TelegramUser, List<Manga>> userMangaList : allUsersThatReadManga.entrySet()) {
            for (Manga manga : userMangaList.getValue()) {

                Chapter lastChapter = manga.getLastChapter();
                String chapterNumber = formatChapterNumber(lastChapter);

                String updateMessage = String.format("\uD83C\uDF1FВышла новая глава\uD83C\uDF1F" +
                                "<b>\n\n%s\n</b>" +
                                "<a href=\"https://cover.imglib.info/uploads/cover/%s/cover/%s_250x350.jpg\">ㅤ\n</a>" +
                                "Том %d Глава %s - %s\n" +
                                "<a href=\"https://mangalib.me/%s/v%d/c%s\">ссылка на главу\n\n</a>" +
                                "<a href=\"https://mangalib.me/%s\">ссылка на тайтл\n\n</a>" +
                                "<b>Внимание если вышло несколько глав, то бот отправит только последнюю главу</b>",
                        manga.getRusName(), manga.getSlug(), manga.getCover(), lastChapter.getVolume(),
                        chapterNumber, lastChapter.getName(), manga.getSlug(), lastChapter.getVolume(),
                        chapterNumber, manga.getSlug());

                messageService.sendMessage(userMangaList.getKey().getChatId(), updateMessage);
            }
        }
    }

    private String formatChapterNumber(Chapter lastChapter) {
        double number = lastChapter.getNumber();
        String s = String.valueOf(number);
        String[] split = s.split("\\.");
        if (Integer.parseInt(split[1]) == 0) {
            s = split[0];
        }
        return s;
    }
}
