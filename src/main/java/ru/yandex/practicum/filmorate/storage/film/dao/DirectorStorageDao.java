package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.entity.Director;

import java.util.List;

public interface DirectorStorageDao {

    List<Director> findAll();

    Director findById(Long genreId);

    List<Director> addToFilm(Long filmId, List<Director> directors);

    List<Director> findAllById(Long filmId);

    boolean existsById(Long directorId);

    Director add(Director director);

    Director update(Director director);

    void deleteDirector(Long directorId);
}
