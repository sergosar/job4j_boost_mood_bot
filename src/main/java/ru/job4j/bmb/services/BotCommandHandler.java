package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;
import ru.job4j.bmb.ui.TgUI;

import java.util.Optional;

@Service
public class BotCommandHandler {
    private final UserRepository userRepository;
    private final MoodService moodService;
    private final TgUI tgUI;

    public BotCommandHandler(UserRepository userRepository,
                             MoodService moodService,
                             TgUI tgUI) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
    }

    Optional<Content> commands(Message message) {
        long chatId = message.getChatId();
        Long clientId = message.getFrom().getId();

        if ("/start".equals(message.getText())) {
            return handleStartCommand(chatId, clientId);
        } else if ("/week_mood_log".equals(message.getText())) {
            return moodService.weekMoodLogCommand(chatId, clientId);
        } else if ("/month_mood_log".equals(message.getText())) {
            return moodService.monthMoodLogCommand(chatId, clientId);
        } else if ("/award".equals(message.getText())) {
            moodService.awards(chatId, clientId);
        }
        return Optional.empty();
    }

    Optional<Content> handleCallback(CallbackQuery callback) {
        var moodId = Long.valueOf(callback.getData());
        var user = userRepository.findById(callback.getFrom().getId());
        return user.map(value -> moodService.chooseMood(value, moodId));
    }

    private Optional<Content> handleStartCommand(long chatId, Long clientId) {
        var user = new User();
        user.setClientId(clientId);
        user.setChatId(chatId);
        userRepository.save(user);
        var content = new Content(user.getChatId());
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }

    void receive(Content content) {
        System.out.println(content);
    }
}
