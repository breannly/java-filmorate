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

    @Override
    public void addMark(Long filmId, Long userId, int mark) {
        jdbcTemplate.update(SQL_QUERY_ADD_MARK, filmId, userId, mark);
    }
}
