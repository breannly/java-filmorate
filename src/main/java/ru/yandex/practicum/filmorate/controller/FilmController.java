package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.config.Config.MAX_SIZE_DESCRIPTION;
import static ru.yandex.practicum.filmorate.config.Config.validateDate;

@Slf4j
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
            log.warn("Название не должно быть пустым");
            throw new ValidationException("Пустое название");
        }

        if (isWrongDuration) {
            log.warn("Продолжительность должна быть положительной");
            throw new ValidationException("Неположительная продолжительность");
        }

        if (isWrongReleaseDate) {
            log.warn("Слишком раняя дата релиза");
            throw new ValidationException("Слишком ранняя дата релиза");
        }

        if (isWrongDescription) {
            log.warn("Описание не должно превышать {} символов", MAX_SIZE_DESCRIPTION);
            throw new ValidationException("Слишком длинное описание");
        }
    }
}
