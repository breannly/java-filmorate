package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping
    public Collection<Film> findAll() {
        return service.findAll();
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        return service.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return service.update(film);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id){
        service.deleteFilm(id);
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable("id") Long filmId) {
        return service.findFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        service.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        service.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(defaultValue = "10") int count,
                                       @RequestParam(required = false) Long genreId,
                                       @RequestParam(defaultValue = "0", required = false) @PositiveOrZero int year
    ) {
        return service.findPopularFilms(count, genreId, year);
    }

    @GetMapping("/common")
    public List<Film> findCommonFilms(@RequestParam("userId") Long userId, @RequestParam("friendId") Long friendId) {
        return service.findCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> findDirectorsFilms(@PathVariable("directorId") Long directorId,
                                         @RequestParam(value = "sortBy", defaultValue = "year") String sortBy) {
        return service.findFilmsByDirector(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> searchFilmsByNameOrDirector(@RequestParam String query,
                                                  @RequestParam @NotNull List<String> by) {
        return service.searchFilmsByNameOrDirector(query, by);
    }
}
