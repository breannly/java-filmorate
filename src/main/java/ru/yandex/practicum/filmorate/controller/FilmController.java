package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@RestController("/films")
public class FilmController {
    List<Film> films = new ArrayList<>();

    @GetMapping
    public List<Film> getFilms() {
        return films;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        films.add(film);

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (films.contains(film)) {
            films.remove(film);
        }
        films.add(film);

        return film;
    }
}
