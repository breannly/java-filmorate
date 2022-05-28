package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.config.Config.MAX_SIZE_DESCRIPTION;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Film extends Entity {
    @NotBlank
    private String name;

    @Size(max = MAX_SIZE_DESCRIPTION, message = "Описание превышает" + MAX_SIZE_DESCRIPTION + "символов")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма отрицательная")
    private long duration;
}
