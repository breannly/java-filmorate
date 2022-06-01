package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.config.Config.MAX_SIZE_DESCRIPTION;
import static ru.yandex.practicum.filmorate.config.Config.validateDate;

@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film> {
    @GetMapping
    public Collection<Film> findAll() {
        return super.findAll();
    }

    @PostMapping
    public Film add(@RequestBody Film film) throws ValidationException {
        return super.add(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        return super.update(film);
    }

    @Override
    protected void validate(Film film) throws ValidationException {
        boolean isWrongName = film.getName().isBlank();
        boolean isWrongDuration = film.getDuration() < 0;
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);
        boolean isWrongDescription = film.getDescription().length() > MAX_SIZE_DESCRIPTION;

        if (isWrongName) {
            throw new ValidationException("Пустое имя");
        }

        if (isWrongDuration) {
            throw new ValidationException("Неположительная продолжительность");
        }

        if (isWrongReleaseDate) {
            throw new ValidationException("Слишком ранняя дата релиза");
        }

        if (isWrongDescription) {
            throw new ValidationException("Слишком длинное описание");
        }
    }
}
