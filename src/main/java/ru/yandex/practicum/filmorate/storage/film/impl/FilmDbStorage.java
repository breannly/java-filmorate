package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.config.Config.*;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorageDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_FILMS, new FilmMapper());
    }

    @Override
    public List<Film> findFilms(int count) {
        return jdbcTemplate.query(SQL_QUERY_FIND_FILMS, new FilmMapper(), count);
    }

    @Override
    public Film findFilmById(Long id) {
        return jdbcTemplate.queryForObject(SQL_QUERY_FIND_FILM, new FilmMapper(), id);
    }

    @Override
    public Film add(Film film) {
        jdbcTemplate.update(SQL_QUERY_ADD_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration());

        return film;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(SQL_QUERY_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());

        return film;
    }
}
