package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.entity.Film;

import java.util.List;

public interface FilmStorageDao {
    public List<Film> findAll();

    public List<Film> findFilms(int count);

    public Film findFilmById(Long id);

    public Film add(Film film);

    public Film update(Film film);
}
