package ru.yandex.practicum.filmorate.model.entity;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

import static ru.yandex.practicum.filmorate.config.Config.MAX_SIZE_DESCRIPTION;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Film extends IdEntity {
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;

    @Size(max = MAX_SIZE_DESCRIPTION, message = "Описание не должно превышать" + MAX_SIZE_DESCRIPTION + "символов")
    private String description;

    @NotNull(message = "Дата релиза должна быть задана")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private long duration;

    @Min(1) @Max(10)
    private Double rate;

    @NotNull
    private Mpa mpa;

    private List<Genre> genres;

    private List<Director> directors;
}