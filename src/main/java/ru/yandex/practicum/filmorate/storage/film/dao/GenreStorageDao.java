package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.entity.Genre;

import java.util.List;

public interface GenreStorageDao {
    public List<Genre> findAll();

    public Genre findGenreById(Long genreId);
}
