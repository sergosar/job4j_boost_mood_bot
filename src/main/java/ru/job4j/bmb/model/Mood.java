package ru.job4j.bmb.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mb_mood")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Mood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private boolean good;

    public Mood(String text, boolean good) {
        this.text = text;
        this.good = good;
    }

}
