package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.model.entity.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.config.Config.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final MpaMapper mpaMapper;

    @Override
    public List<Film> findAll() {
        try {
            List<Film> films = jdbcTemplate.query(SQL_QUERY_FIND_ALL_FILMS, filmMapper);
            films.forEach(film -> film.setGenres(findGenreById(film.getId())));
            return films;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Film> findFilms(int count) {
        try {
            List<Film> films = jdbcTemplate.query(SQL_QUERY_FIND_FILMS, filmMapper, count);
            films.forEach(film -> film.setGenres(findGenreById(film.getId())));
            return films;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Film findFilmById(Long id) {
        try {
            Film film = jdbcTemplate.queryForObject(SQL_QUERY_FIND_FILM_BY_ID, filmMapper, id);
            film.setGenres(findGenreById(film.getId()));
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }

    private List<Genre> findGenreById(Long filmId) {
        List<Genre> genres = jdbcTemplate.query(SQL_QUERY_FIND_GENRES, new GenreMapper(), filmId);
        return genres;
    }

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_QUERY_ADD_FILM, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3, film.getReleaseDate());
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        film.setMpa(setMpa(film.getId()));

        if (film.getGenres() != null) {
            film.setGenres(addGenres(film.getId(), film.getGenres()));
        }
        return film;
    }

    private Mpa setMpa(Long filmId) {
        return jdbcTemplate.queryForObject(SQL_QUERY_FIND_MPA, mpaMapper, filmId);
    }

    private List<Genre> addGenres(Long filmId, List<Genre> genres) {
        genres.forEach(genre -> jdbcTemplate.update(SQL_QUERY_ADD_GENRE, filmId, genre.getId()));
        return findGenreById(filmId);
    }

    @Override
    public Film update(Film film) {
        int count = jdbcTemplate.update(SQL_QUERY_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (count == 0) {
            throw new ObjectNotFoundException("Вызов несуществующего ответа");
        }

        film.setMpa(setMpa(film.getId()));

        if (film.getGenres() != null) {
            jdbcTemplate.update(SQL_QUERY_DELETE_GENRES, film.getId());
            film.setGenres(addGenres(film.getId(), film.getGenres()));
        }
        return film;
    }
}
