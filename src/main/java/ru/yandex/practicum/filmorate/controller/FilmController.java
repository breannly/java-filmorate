package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.generator.GeneratorFilmId;
import ru.yandex.practicum.filmorate.model.Film;

import javax.servlet.http.HttpServletRequest;
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
    public Film addFilm(@RequestBody Film film, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        checkValidation(film);

        films.put(createFilm(film), film);
        return film;
    }

    private Long createFilm(Film film) {
        film.setId(generatorId.generate());

        return film.getId();
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        checkValidation(film);

        if (!films.containsKey(film.getId())) {
            log.info("Неверно указан id");
            throw new ValidationException("Пользователя с таким id нет");
        }
        films.put(film.getId(), film);

        return film;
    }

    private void checkValidation(Film film) throws ValidationException {
        boolean isWrongName = film.getName().isBlank();
        boolean isWrongDescription = film.getDescription().length() > 200;
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);
        boolean isWrongDuration = film.getDuration() < 0;

        if (isWrongName || isWrongDuration || isWrongDescription || isWrongReleaseDate) {
            log.info("Запрос не прошел Film-валидацию");
            throw new ValidationException("Фильм не соответствует критериям");
        }
    }
}
