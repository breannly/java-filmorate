package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.dao.LikeStorageDao;

import static ru.yandex.practicum.filmorate.config.Config.*;

@Component
public class LikeDbStorage implements LikeStorageDao {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        if (isUserExists(userId) && isFilmExists(filmId)) {
            jdbcTemplate.update(SQL_QUERY_ADD_LIKE, filmId, userId);
        } else throw new ObjectNotFoundException("Вызов несуществующего объекта");
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        if (isUserExists(userId) && isFilmExists(filmId)) {
            jdbcTemplate.update(SQL_QUERY_DELETE_LIKE, filmId, userId);
        } else throw new ObjectNotFoundException("Вызов несуществующего объекта");
    }

    private boolean isUserExists(Long userId) {
        int count = jdbcTemplate.queryForObject(SQL_QUERY_CHECK_USER, Integer.class, userId);
        return count > 0;
    }

    private boolean isFilmExists(Long filmId) {
        int count = jdbcTemplate.queryForObject(SQL_QUERY_CHECK_FILM, Integer.class, filmId);
        return count > 0;
    }
}
