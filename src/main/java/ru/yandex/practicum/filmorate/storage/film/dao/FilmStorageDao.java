package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.entity.Film;

import java.util.List;

public interface FilmStorageDao {
    List<Film> findAll();

    List<Film> findPopularFilms(int count, Long genreId, int year);

    Film findById(Long filmId);

    Film add(Film film);

    Film update(Film film);

    void deleteFilm(Long filmId);

    boolean existsById(Long filmId);

    List<Film> findCommonFilmsForUsers(Long userId, Long friendId);

	List<Film> findFilmsByDirector(Long directorId, String sortBy);

    List<Film> getRecommendations(Long userId);

    List<Film> searchFilmsByNameOrDirector(String textQuery, List<String> searchParams);
}