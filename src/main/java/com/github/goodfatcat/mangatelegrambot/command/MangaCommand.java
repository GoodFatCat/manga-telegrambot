package com.github.goodfatcat.mangatelegrambot.command;

import com.github.goodfatcat.mangatelegrambot.exception.NoSuchUserException;
import com.github.goodfatcat.mangatelegrambot.model.Items;
import com.github.goodfatcat.mangatelegrambot.repository.entity.Manga;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import com.github.goodfatcat.mangatelegrambot.repository.entity.UserManga;
import com.github.goodfatcat.mangatelegrambot.service.MangaService;
import com.github.goodfatcat.mangatelegrambot.service.SendBotMessageService;
import com.github.goodfatcat.mangatelegrambot.service.TelegramUserService;
import com.github.goodfatcat.mangatelegrambot.service.UserMangaService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MangaCommand {@link Command}
 */
public class MangaCommand implements Command {

    private SendBotMessageService sendBotMessageService;
    private MangaService mangaService;
    private TelegramUserService telegramUserService;
    private UserMangaService userMangaService;

    private static final String LINK_PREFIX = "https://mangalib.me/user/";
    public static final String ERROR_NO_LINK_MESSAGE = "Введите ссылку на ваш профиль. " +
            "Пример: \"/manga https://mangalib.me/user/(ваш id)\".";
    public static final String ERROR_LINK_MESSAGE = "Ошибка ссылки. Сссылка должна " +
            "выглядеть так: \"https://mangalib.me/user/(ваш id)\"." +
            " Или вы можете использовать только id";
    public static final String ERROR_NO_USER_MESSAGE = "Такого пользователя нет или у вас нет закладок.";
    public static final String SUCCESS_MESSAGE = "Вы успешно добавили мангу, " +
            "скоро вы получите оповещение об обновлениях.";


    public MangaCommand(SendBotMessageService sendBotMessageService,
                        MangaService mangaService,
                        TelegramUserService telegramUserService,
                        UserMangaService userMangaService) {
        this.sendBotMessageService = sendBotMessageService;
        this.mangaService = mangaService;
        this.telegramUserService = telegramUserService;
        this.userMangaService = userMangaService;
    }

    @Override
    public void execute(Update update) {
        String telegramUserId = update.getMessage().getChatId().toString();
        int mangaId;
        try {
            mangaId = verifyLink(update);
        } catch (ArrayIndexOutOfBoundsException e) {
            sendBotMessageService.sendMessage(telegramUserId, ERROR_NO_LINK_MESSAGE);
            return;
        } catch (NumberFormatException e) {
            sendBotMessageService.sendMessage(telegramUserId, ERROR_LINK_MESSAGE);
            return;
        }

        Items jsonWrap;
        try {
            jsonWrap = mangaService.getBookmarkFromWeb(mangaId);
        } catch (NoSuchUserException e) {
            sendBotMessageService.sendMessage(telegramUserId, ERROR_NO_USER_MESSAGE);
            return;
        }
        List<Manga> mangaList = getMangaList(jsonWrap);
        List<UserManga> userMangaList = getUserMangaList(telegramUserId, jsonWrap);

        updateOrCreateTelegramUser(telegramUserId, mangaId);

        mangaService.saveAll(mangaList);
        userMangaService.saveAll(userMangaList);

        sendBotMessageService.sendMessage(telegramUserId, SUCCESS_MESSAGE);
    }

    private int verifyLink(Update update) {
        String mangalibLink = update.getMessage().getText().split(" ")[1];
        int userId;
        if (mangalibLink.startsWith(LINK_PREFIX)) {
            userId = Integer.parseInt(mangalibLink.split(LINK_PREFIX)[1]);
        } else {
            userId = Integer.parseInt(mangalibLink);
        }
        return userId;
    }

    private void updateOrCreateTelegramUser(String telegramUserId, int mangaId) {
        telegramUserService.findByChatId(telegramUserId).ifPresentOrElse(
                telegramUser -> {
                    telegramUser.setMangaId(mangaId);
                    telegramUserService.save(telegramUser);
                },
                () -> {
                    TelegramUser telegramUser = new TelegramUser();
                    telegramUser.setActive(true);
                    telegramUser.setChatId(telegramUserId);
                    telegramUser.setMangaId(mangaId);
                    telegramUserService.save(telegramUser);
                }
        );
    }

    private static List<Manga> getMangaList(Items items) {
        return items.getMangaSet().stream()
                .map(Manga::getInstanceFromJsonManga)
                .collect(Collectors.toList());
    }

    private static List<UserManga> getUserMangaList(String telegramUserId, Items items) {
        return items.getMangaSet()
                .stream()
                .map(manga -> new UserManga(manga.getId(), telegramUserId, manga.getStatus()))
                .collect(Collectors.toList());
    }
}
