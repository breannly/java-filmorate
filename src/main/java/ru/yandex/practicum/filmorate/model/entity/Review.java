package ru.yandex.practicum.filmorate.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Review {
    private Long reviewId;
    private String content;
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private Integer useful;
}
