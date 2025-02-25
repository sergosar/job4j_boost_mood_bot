package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.exception.SentContentException;
import ru.job4j.bmb.ui.TgUI;

@Service
public class TelegramBotService extends TelegramLongPollingBot implements SentContent {
    private final TgUI tgUI;
    private final BotCommandHandler handler;
    private final String botName;

    private final String botToken;

    public TelegramBotService(@Value("${telegram.bot.name}") String botName,
                              @Value("${telegram.bot.token}") String botToken,
                              TgUI tgUI, BotCommandHandler handler) {
        super(botToken);
        this.tgUI = tgUI;
        this.handler = handler;
        this.botName = botName;
        this.botToken = botToken;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handler.handleCallback(update.getCallbackQuery())
                    .ifPresent(this::sent);
        } else if (update.hasMessage() && update.getMessage().getText() != null) {
            handler.commands(update.getMessage())
                    .ifPresent(this::sent);
        }
    }

    @Override
    public void sent(Content content) {
        try {
            if (content.getAudio() != null) {
                execute(new SendAudio(content.getChatId().toString(), content.getAudio()));
            } else if (content.getText() != null) {
                SendMessage message = new SendMessage(content.getChatId().toString(), content.getText());
                if (content.getMarkup() != null) {
                    message.setReplyMarkup(content.getMarkup());
                }
                execute(message);
            } else if (content.getPhoto() != null) {
                execute(new SendPhoto(content.getChatId().toString(), content.getPhoto()));
            }
        } catch (TelegramApiException e) {
            throw new SentContentException("Ошибка отправки контента", e);
        }
    }

    public void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
