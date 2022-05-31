package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.config.Config.validateDate;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {

    @GetMapping
    public Collection<Film> findAll() {
        return super.findAll();
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) throws ValidationException {
        return super.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        return super.update(film);
    }

    @Override
    protected void validate(Film film) throws ValidationException {
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);

        if (isWrongReleaseDate) {
            log.warn("Слишком ранняя дата релиза у объекта {}", film);
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }
}
