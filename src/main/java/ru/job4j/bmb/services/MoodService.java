package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repositories.AchievementRepository;
import ru.job4j.bmb.repositories.MoodLogRepository;
import ru.job4j.bmb.repositories.MoodRepository;
import ru.job4j.bmb.repositories.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MoodService {
    private final MoodLogRepository moodLogRepository;
    private final MoodRepository moodRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       MoodRepository moodRepository, RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository) {
        this.moodLogRepository = moodLogRepository;
        this.moodRepository = moodRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        Optional<Mood> mood = moodRepository.findById(moodId);
        moodLogRepository.save(new MoodLog(user, mood.get(), Instant.now().getEpochSecond()));
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        content.setText(formatMoodLogs(getMoodLogsOfPeriod(chatId, clientId, 60 * 60 * 24 * 7), "отчет по настроению за семь дней: "));
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        content.setText(formatMoodLogs(getMoodLogsOfPeriod(chatId, clientId, 60 * 60 * 24 * 30), "отчет по настроению за месяц: "));
        return Optional.of(content);
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\nNo mood logs found.";
        }
        var sb = new StringBuilder(title + ":\n");
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            sb.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
        });
        return sb.toString();
    }

    private List<MoodLog> getMoodLogsOfPeriod(long chatId, Long clientId, long seconds) {
        User user = userRepository.findAll().stream()
                .filter(u -> Objects.equals(u.getClientId(), clientId) && Objects.equals(u.getChatId(), chatId))
                .findFirst().orElse(null);
        return moodLogRepository.findAll().stream()
                .filter(log -> Objects.equals(log.getUser(), user) && (Instant.now().getEpochSecond() - log.getCreatedAt()) <= seconds)
                .toList();
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        var content = new Content(chatId);
        User user = userRepository.findAll().stream().filter(u -> Objects.equals(u.getClientId(), clientId)).findFirst().get();
        List<String> awards = achievementRepository.findAll().stream()
                .filter(a -> Objects.equals(a.getUser(), user))
                .map(Achievement::getAward)
                .map(award -> String.format("%s : %s", award.getTitle(), award.getDescription()))
                .toList();
        content.setText(String.join("\n", awards));
        return Optional.of(content);
    }
}

