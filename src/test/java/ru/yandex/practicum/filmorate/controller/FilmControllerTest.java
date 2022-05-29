package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmControllerTest {
    private final FilmController controller;

    public FilmControllerTest() {
        controller = new FilmController();
    }

    @Test
    void shouldThrowValidationExceptionWhenWrongReleaseDate() {
        LocalDate wrongReleaseDate = LocalDate.of(1200, 12, 12);
        Film film = getDefaultFilm();
        film.setReleaseDate(wrongReleaseDate);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> {controller.add(film);}
        );
        Assertions.assertEquals("Фильм не соответствует критериям", exception.getMessage());
    }

    private Film getDefaultFilm() {
        Film film = new Film("test",
                "test",
                LocalDate.of(2022, 12, 12),
                120);

        return film;
    }
}