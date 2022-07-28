package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.film.dao.MarkStorageDao;

@Repository
@RequiredArgsConstructor
public class MarkDbStorage implements MarkStorageDao {
    private final JdbcTemplate jdbcTemplate;

    public static final String SQL_QUERY_ADD_MARK = "INSERT INTO FILM_MARKS (FILM_ID, USER_ID, FILM_MARK) VALUES (?, ?, ?)";
    public static final String SQL_QUERY_DELETE_MARK = "DELETE FROM FILM_MARKS WHERE FILM_ID = ? AND user_id = ?";
    private static final String SQL_QUERY_GET_FILM_AVG_RATE = "SELECT AVG(FILM_MARK) FROM FILM_MARKS WHERE FILM_ID = ?";
    public static final String SQL_QUERY_UPDATE_FILM_MARK = "UPDATE FILMS SET RATE = ? WHERE FILM_ID = ?";

    @Override
    public void addMark(Long filmId, Long userId, int mark) {
        jdbcTemplate.update(SQL_QUERY_ADD_MARK, filmId, userId, mark);
    }

    @Override
    public void deleteMark(Long filmId, Long userId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_MARK, filmId, userId);
    }

    @Override
    public void updateFilmAverageRate(Long filmId) {
        Double rate = jdbcTemplate.queryForObject(SQL_QUERY_GET_FILM_AVG_RATE, Double.class, filmId);
        jdbcTemplate.update(SQL_QUERY_UPDATE_FILM_MARK, rate, filmId);
    }

}
