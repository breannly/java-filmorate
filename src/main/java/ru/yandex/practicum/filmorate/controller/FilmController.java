package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.generator.GeneratorFilmId;
import ru.yandex.practicum.filmorate.model.Film;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films;
    private final GeneratorFilmId generatorId;
    private static final LocalDate validateDate = LocalDate.of(1895, 12, 28);

    public FilmController() {
        films = new HashMap<>();
        generatorId = new GeneratorFilmId();
    }

    @GetMapping
    public Collection<Film> getFilms(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        validate(film);

        films.put(createFilm(film), film);
        return film;
    }

    private Long createFilm(Film film) {
        film.setId(generatorId.generate());

        return film.getId();
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        validate(film);

        if (!films.containsKey(film.getId())) {
            log.info("Неверно указан id");
            throw new ValidationException("Пользователя с таким id нет");
        }
        films.put(film.getId(), film);

        return film;
    }

    private void validate(Film film) throws ValidationException {
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);

        if (isWrongReleaseDate) {
            log.info("Дата релиза раньше 1895 года");
            throw new ValidationException("Фильм не соответствует критериям");
        }
    }
}
