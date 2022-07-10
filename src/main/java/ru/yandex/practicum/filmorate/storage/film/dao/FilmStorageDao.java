package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.entity.Film;

import java.util.List;

public interface FilmStorageDao {
    List<Film> findAll();

    List<Film> findFilms(int count);

    Film findById(Long id);

    Film add(Film film);

    Film update(Film film);

    boolean existsById(Long id);
}
