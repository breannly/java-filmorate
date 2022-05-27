package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Film {
    private Long id;
    private String description;
    private LocalDateTime releaseDate;
    private long duration;
}
