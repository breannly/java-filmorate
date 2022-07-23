package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreStorageDao;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    public static final String SQL_QUERY_FIND_ALL_GENRES = "SELECT * FROM GENRES";
    public static final String SQL_QUERY_FIND_GENRE_BY_ID = "SELECT * FROM GENRES WHERE genre_id = ?";
    public static final String SQL_QUERY_FIND_GENRES = "SELECT * FROM FILM_GENRES AS fg " +
            "JOIN GENRES as g ON fg.genre_id = g.genre_id WHERE film_id = ?";
    public static final String SQL_QUERY_ADD_GENRE = "MERGE INTO FILM_GENRES" +
            "(film_id, genre_id) values (?, ?)";
    public static final String SQL_QUERY_DELETE_GENRES = "DELETE FROM FILM_GENRES WHERE film_id = ?";
    public static final String SQL_QUERY_CHECK_GENRE = "SELECT COUNT(*) FROM GENRES WHERE GENRE_ID = ?";

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_GENRES, genreMapper);
    }

    @Override
    public Genre findById(Long genreId) {
        return jdbcTemplate.queryForObject(SQL_QUERY_FIND_GENRE_BY_ID, genreMapper, genreId);
    }

    @Override
    public List<Genre> addToFilm(Long filmId, List<Genre> genres) {
        jdbcTemplate.update(SQL_QUERY_DELETE_GENRES, filmId);
        if (genres != null) {
            genres.forEach(genre -> jdbcTemplate.update(SQL_QUERY_ADD_GENRE, filmId, genre.getId()));
        }
        return findAllById(filmId);
    }

    @Override
    public List<Genre> findAllById(Long filmId) {
        return jdbcTemplate.query(SQL_QUERY_FIND_GENRES, genreMapper, filmId);
    }

    @Override
    public boolean existsById(Long genreId) {
        Integer count = jdbcTemplate.queryForObject(SQL_QUERY_CHECK_GENRE, Integer.class, genreId);
        assert count != null;
        return count.equals(1);
    }
}
