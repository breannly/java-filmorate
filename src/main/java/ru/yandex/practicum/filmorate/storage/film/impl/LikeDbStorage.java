package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.film.dao.LikeStorageDao;

import static ru.yandex.practicum.filmorate.config.Config.SQL_QUERY_ADD_LIKE;
import static ru.yandex.practicum.filmorate.config.Config.SQL_QUERY_DELETE_LIKE;

@Component
public class LikeDbStorage implements LikeStorageDao {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Long id, Long filmId) {
        jdbcTemplate.update(SQL_QUERY_ADD_LIKE, filmId, id);
    }

    @Override
    public void deleteLike(Long id, Long filmId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_LIKE, filmId, id);
    }
}
