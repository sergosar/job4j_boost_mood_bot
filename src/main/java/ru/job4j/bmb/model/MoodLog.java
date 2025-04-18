package ru.job4j.bmb.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mb_mood_log")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class MoodLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    private long createdAt;

    public MoodLog(User user, Mood mood, long createdAt) {
        this.user = user;
        this.mood = mood;
        this.createdAt = createdAt;
    }
}

