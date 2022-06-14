package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.config.Config.validateDate;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage inMemoryFilmStorage;
    private final FilmService service;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService service) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.service = service;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        validate(film);
        return inMemoryFilmStorage.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validate(film);
        return inMemoryFilmStorage.update(film);
    }

    private void validate(Film film) {
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);

        if (isWrongReleaseDate) {
            log.warn("Слишком раняя дата релиза");
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return service.findFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilms(@RequestParam(defaultValue = "10") int count) {
        return service.getFilms(count);
    }
}
