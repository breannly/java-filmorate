package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.generator.GeneratorFilmId;
import ru.yandex.practicum.filmorate.model.Film;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.config.Config.validateDate;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends Controller<Film> {
    private final GeneratorFilmId generatorId;

    public FilmController() {
        generatorId = new GeneratorFilmId();
    }

    @GetMapping
    public Collection<Film> findAll() {
        return super.findAll();
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) throws ValidationException {
        validate(film);

        return super.add(createFilm(film));
    }

    private Film createFilm(Film film) {
        film.setId(generatorId.generate());

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        validate(film);

        return super.update(film);
    }

    private void validate(Film film) throws ValidationException {
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);

        if (isWrongReleaseDate) {
            log.info("Дата релиза раньше 1895 года");
            throw new ValidationException("Фильм не соответствует критериям");
        }
    }
}
