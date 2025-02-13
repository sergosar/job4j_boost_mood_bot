
package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;
    private final UserRepository userRepository;

    public TgRemoteService(@Value("${telegram.bot.name}") String botName,
                           @Value("${telegram.bot.token}") String botToken,
                           UserRepository userRepository) {
        this.botName = botName;
        this.botToken = botToken;
        this.userRepository = userRepository;
    }

    private final static Map<String, String> MOOD_RESP = new HashMap<>();

    static {
        MOOD_RESP.put("lost_sock", "Носки — это коварные создания. Но не волнуйся, второй обязательно найдётся!");
        MOOD_RESP.put("cucumber", "Огурец тоже дело серьёзное! Главное, не мариноваться слишком долго.");
        MOOD_RESP.put("dance_ready", "Супер! Танцуй, как будто никто не смотрит. Или, наоборот, как будто все смотрят!");
        MOOD_RESP.put("need_coffee", "Кофе уже в пути! Осталось только подождать... И ещё немного подождать...");
        MOOD_RESP.put("sleepy", "Пора на боковую! Даже супергерои отдыхают, ты не исключение.");
        MOOD_RESP.put("angry", "Не парься, просто терпи а то может быть хуже, а ТАНЯ редиска!.");
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            if ("/start".equals(message.getText())) {
                long chatId = message.getChatId();
                var user = new User();
                user.setClientId(message.getFrom().getId());
                user.setChatId(chatId);
                userRepository.save(user);
                send(sendButtons(chatId));
            }
        }
        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            var chatId = update.getCallbackQuery().getMessage().getChatId();
            send(new SendMessage(String.valueOf(chatId), MOOD_RESP.get(data)));
        }
    }

    public void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public SendMessage sendButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Как настроение сегодня?");

        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(createBtn("Потерял носок \uD83D\uDE22", "lost_sock")));
        keyboard.add(List.of(createBtn("Как огурец на полке \uD83D\uDE10", "cucumber")));
        keyboard.add(List.of(createBtn("Готов к танцам \uD83D\uDE04", "dance_ready")));
        keyboard.add(List.of(createBtn("Где мой кофе?! \uD83D\uDE23", "need_coffee")));
        keyboard.add(List.of(createBtn("Слипаются глаза \uD83D\uDE29", "sleepy")));
        keyboard.add(List.of(createBtn("Достала жена Татьяна \uD83E\uDD2C", "angry")));

        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }

    InlineKeyboardButton createBtn(String name, String data) {
        var inline = new InlineKeyboardButton();
        inline.setText(name);
        inline.setCallbackData(data);
        return inline;
    }

}