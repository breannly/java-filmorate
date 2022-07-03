package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.config.Config.MAX_SIZE_DESCRIPTION;

@Getter
@Setter
@AllArgsConstructor
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

    private final Long rate;

    private final Set<Long> likes = new HashSet<>();

}
