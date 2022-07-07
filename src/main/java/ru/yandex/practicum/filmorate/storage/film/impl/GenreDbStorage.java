package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreStorageDao;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.config.Config.*;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_GENRES, genreMapper);
    }

    @Override
    public Genre findGenreById(Long genreId) {
        try {
            return jdbcTemplate.queryForObject(SQL_QUERY_FIND_GENRE_BY_ID, genreMapper, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }
}
