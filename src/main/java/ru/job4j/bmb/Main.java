package ru.job4j.bmb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodContentRepository;
import ru.job4j.bmb.repository.MoodRepository;

import java.util.ArrayList;

@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner loadDatabase(MoodRepository moodRepository,
                                   MoodContentRepository moodContentRepository,
                                   AwardRepository awardRepository) {
        return args -> {
            var moods = moodRepository.findAll();
            if (!moods.isEmpty()) {
                return;
            }
            var data = new ArrayList<MoodContent>();
            fillMoodContent(data);

            moodRepository.saveAll(data.stream().map(MoodContent::getMood).toList());
            moodContentRepository.saveAll(data);

            var awards = new ArrayList<Award>();
            fillAwards(awards);

            awardRepository.saveAll(awards);
        };
    }

    private void fillAwards(ArrayList<Award> awards) {
        awards.add(new Award("Смайлик дня", "За 1 день хорошего настроения.", 1));
        awards.add(new Award("Настроение недели", "За 7 последовательных дней хорошего или отличного настроения.", 7));
        awards.add(new Award("Бонусные очки", "За каждые 3 дня хорошего настроения.", 3));
        awards.add(new Award("Персонализированные рекомендации", "После 5 дней хорошего настроения.", 5));
        awards.add(new Award("Достижение 'Солнечный луч'", "За 10 дней непрерывного хорошего настроения.", 10));
        awards.add(new Award("Виртуальный подарок", "После 15 дней хорошего настроения.", 15));
        awards.add(new Award("Титул 'Лучезарный'", "За 20 дней хорошего или отличного настроения.", 20));
        awards.add(new Award("Доступ к премиум-функциям", "После 30 дней хорошего настроения.", 30));
        awards.add(new Award("Участие в розыгрыше призов", "За каждую неделю хорошего настроения.", 7));
        awards.add(new Award("Эксклюзивный контент", "После 25 дней хорошего настроения.", 25));
        awards.add(new Award("Награда 'Настроение месяца'", "За поддержание хорошего или отличного настроения в течение целого месяца.", 30));
        awards.add(new Award("Физический подарок", "После 60 дней хорошего настроения.", 60));
        awards.add(new Award("Коучинговая сессия", "После 45 дней хорошего настроения.", 45));
        awards.add(new Award("Разблокировка мини-игр", "После 14 дней хорошего настроения.", 14));
        awards.add(new Award("Персональное поздравление", "За значимые достижения (например, 50 дней хорошего настроения).", 50));
    }

    private void fillMoodContent(ArrayList<MoodContent> data) {
        data.add(new MoodContent(
                new Mood("Счастливейший на свете \uD83D\uDE0E", true),
                "Невероятно! Вы сияете от счастья, продолжайте радоваться жизни."));
        data.add(new MoodContent(
                new Mood("Воодушевленное настроение 🌟", true),
                "Великолепно! Вы чувствуете себя на высоте. Продолжайте в том же духе."));
        data.add(new MoodContent(
                new Mood("Успокоение и гармония 🧘‍♂️", true),
                "Потрясающе! Вы в состоянии внутреннего мира и гармонии."));
        data.add(new MoodContent(
                new Mood("В состоянии комфорта ☺️", true),
                "Отлично! Вы чувствуете себя уютно и спокойно."));
        data.add(new MoodContent(
                new Mood("Легкое волнение 🎈", true),
                "Замечательно! Немного волнения добавляет жизни краски."));
        data.add(new MoodContent(
                new Mood("Сосредоточенное настроение 🎯", true),
                "Хорошо! Ваш фокус на высоте, используйте это время эффективно."));
        data.add(new MoodContent(
                new Mood("Тревожное настроение 😟", false),
                "Не волнуйтесь, всё пройдет. Попробуйте расслабиться и найти источник вашего беспокойства."));
        data.add(new MoodContent(
                new Mood("Разочарованное настроение 😞", false),
                "Бывает. Не позволяйте разочарованию сбить вас с толку, всё наладится."));
        data.add(new MoodContent(
                new Mood("Усталое настроение 😴", false),
                "Похоже, вам нужен отдых. Позаботьтесь о себе и отдохните."));
        data.add(new MoodContent(
                new Mood("Вдохновенное настроение 💡", true),
                "Потрясающе! Вы полны идей и энергии для их реализации."));
        data.add(new MoodContent(
                new Mood("Раздраженное настроение 😠", true),
                "Попробуйте успокоиться и найти причину раздражения, чтобы исправить ситуацию."));
    }
}

