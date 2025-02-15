package ru.job4j.bmb.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mb_award")
@Getter
@Setter
@EqualsAndHashCode
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int days;

    public Award() {
    }

    public Award(String title, String description, int days) {
        this.title = title;
        this.description = description;
        this.days = days;
    }
}
