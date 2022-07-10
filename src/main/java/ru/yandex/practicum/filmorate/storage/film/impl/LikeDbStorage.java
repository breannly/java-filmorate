package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.film.dao.LikeStorageDao;

@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorageDao {
    private final JdbcTemplate jdbcTemplate;

    public static final String SQL_QUERY_ADD_LIKE = "INSERT INTO FILM_LIKES (film_id, user_id) VALUES (?, ?)";
    public static final String SQL_QUERY_DELETE_LIKE = "DELETE FROM FILM_LIKES WHERE film_id = ? AND user_id = ?";

    @Override
    public void addLike(Long filmId, Long userId) {
        jdbcTemplate.update(SQL_QUERY_ADD_LIKE, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_LIKE, filmId, userId);
    }
}
