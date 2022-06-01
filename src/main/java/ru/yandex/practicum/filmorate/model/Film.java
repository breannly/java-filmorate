package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Film extends IdEntity {
    private String name;
    private String descriptions;
    private LocalDate releaseDate;
    private long duration;
}
