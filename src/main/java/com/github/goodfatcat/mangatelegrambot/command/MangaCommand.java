package com.github.goodfatcat.mangatelegrambot.command;

import com.github.goodfatcat.mangatelegrambot.exception.NoSuchUserException;
import com.github.goodfatcat.mangatelegrambot.repository.entity.TelegramUser;
import com.github.goodfatcat.mangatelegrambot.service.MangaService;
import com.github.goodfatcat.mangatelegrambot.service.SendBotMessageService;
import com.github.goodfatcat.mangatelegrambot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

/**
 * MangaCommand {@link Command}
 */

@Slf4j
public class MangaCommand implements Command {

    private SendBotMessageService sendBotMessageService;
    private MangaService mangaService;
    private TelegramUserService telegramUserService;

    private static final String LINK_PREFIX = "https://mangalib\\.me/user/";
    public static final String ERROR_NO_LINK_MESSAGE = "Введите ссылку на ваш профиль. " +
            "Пример: \"/manga https://mangalib.me/user/(ваш id)\".";
    public static final String ERROR_LINK_MESSAGE = "Ошибка ссылки. Сссылка должна " +
            "выглядеть так: \"https://mangalib.me/user/(ваш id)\"." +
            " Или вы можете использовать только id";
    public static final String ERROR_NO_USER_MESSAGE = "Такого пользователя нет или у вас нет закладок.";
    public static final String MANGALIB_SERVER_ERROR_MESSAGE = "Во время подключения к сайту " +
            "mangalib произошла ошибка, возможно на сайте проводяться технические работы.";
    public static final String SUCCESS_MESSAGE = "Вы успешно добавили мангу, " +
            "вы будете получать оповещения из списка читаю. Бот будет автоматически обновлять список, " +
            "обновлять его самостоятельно не надо.";



    public MangaCommand(SendBotMessageService sendBotMessageService, MangaService mangaService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.mangaService = mangaService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        String telegramUserId = update.getMessage().getChatId().toString();
        long mangalibId;
        try {
            mangalibId = verifyLink(update);
        } catch (ArrayIndexOutOfBoundsException e) {
            sendBotMessageService.sendMessage(telegramUserId, ERROR_NO_LINK_MESSAGE);
            return;
        } catch (NumberFormatException e) {
            sendBotMessageService.sendMessage(telegramUserId, ERROR_LINK_MESSAGE);
            return;
        }

        try {
            mangaService.getMangasByMangalibId(mangalibId);
        } catch (NoSuchUserException e) {
            sendBotMessageService.sendMessage(telegramUserId, ERROR_NO_USER_MESSAGE);
            return;
        } catch (RuntimeException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            sendBotMessageService.sendMessage(telegramUserId, MANGALIB_SERVER_ERROR_MESSAGE);
            return;
        }

        updateOrCreateTelegramUser(telegramUserId, mangalibId);

        sendBotMessageService.sendMessage(telegramUserId, SUCCESS_MESSAGE);
    }

    private long verifyLink(Update update) {
        String mangalibLink = update.getMessage().getText().split(" ")[1];
        long userId;
        if (mangalibLink.startsWith(LINK_PREFIX)) {
            userId = Long.parseLong(mangalibLink.split(LINK_PREFIX)[1]);
        } else {
            userId = Long.parseLong(mangalibLink);
        }
        return userId;
    }

    private void updateOrCreateTelegramUser(String telegramUserId, long mangaId) {
        telegramUserService.findByChatId(telegramUserId).ifPresentOrElse(
                telegramUser -> {
                    telegramUser.setMangalibId(mangaId);
                    telegramUserService.save(telegramUser);
                },
                () -> {
                    TelegramUser telegramUser = new TelegramUser();
                    telegramUser.setActive(true);
                    telegramUser.setChatId(telegramUserId);
                    telegramUser.setMangalibId(mangaId);
                    telegramUserService.save(telegramUser);
                }
        );
    }
}
