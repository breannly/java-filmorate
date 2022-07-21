package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.entity.Director;
import ru.yandex.practicum.filmorate.storage.film.dao.DirectorStorageDao;
import ru.yandex.practicum.filmorate.storage.mapper.DirectorMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final DirectorMapper directorMapper;

    public static final String SQL_QUERY_FIND_ALL_DIRECTORS = "SELECT * FROM DIRECTORS";
    public static final String SQL_QUERY_FIND_DIRECTOR_BY_ID = "SELECT * FROM DIRECTORS WHERE director_id = ?";
    public static final String SQL_QUERY_FIND_DIRECTORS = "SELECT * FROM FILM_DIRECTORS AS fd " +
            "JOIN DIRECTORS as d ON fd.director_id = d.director_id WHERE fd.film_id = ?";
    public static final String SQL_QUERY_ADD_DIRECTOR_TO_FILM = "MERGE INTO FILM_DIRECTORS" +
            "(film_id, director_id) values (?, ?)";
    public static final String SQL_QUERY_DELETE_DIRECTORS = "DELETE FROM FILM_DIRECTORS WHERE film_id = ?";
    public static final String SQL_QUERY_CHECK_DIRECTOR = "SELECT COUNT(*) FROM DIRECTORS WHERE director_id = ?";

    public static final String SQL_QUERY_ADD_DIRECTOR = "INSERT INTO DIRECTORS (name) values (?)";

    public static final String SQL_QUERY_UPDATE_DIRECTOR = "UPDATE DIRECTORS SET name = ? WHERE director_id = ?";

    public static final String SQL_QUERY_DELETE_DIRECTOR = "DELETE FROM DIRECTORS WHERE director_id = ?";

    @Override
    public List<Director> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_DIRECTORS, directorMapper);
    }

    @Override
    public Director findById(Long directorId) {
        return jdbcTemplate.queryForObject(SQL_QUERY_FIND_DIRECTOR_BY_ID, directorMapper, directorId);
    }

    @Override
    public List<Director> addToFilm(Long filmId, List<Director> directors) {
        jdbcTemplate.update(SQL_QUERY_DELETE_DIRECTORS, filmId);
        directors.forEach(director -> jdbcTemplate.update(SQL_QUERY_ADD_DIRECTOR_TO_FILM, filmId, director.getId()));
        return findAllById(filmId);
    }

    @Override
    public List<Director> findAllById(Long filmId) {
        return jdbcTemplate.query(SQL_QUERY_FIND_DIRECTORS, directorMapper, filmId);
    }

    @Override
    public boolean existsById(Long directorId) {
        Integer count = jdbcTemplate.queryForObject(SQL_QUERY_CHECK_DIRECTOR, Integer.class, directorId);
        assert count != null;
        return count.equals(1);
    }

    @Override
    public Director add(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_QUERY_ADD_DIRECTOR, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return director;
    }

    @Override
    public Director update(Director director) {
        jdbcTemplate.update(SQL_QUERY_UPDATE_DIRECTOR,
                director.getName(),
                director.getId());
        return director;
    }

    @Override
    public void deleteDirector(Long id) {
        jdbcTemplate.update(SQL_QUERY_DELETE_DIRECTOR, id);
    }
}
