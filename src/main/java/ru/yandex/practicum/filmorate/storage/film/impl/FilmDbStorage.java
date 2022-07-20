package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    public static final String SQL_QUERY_FIND_ALL_FILMS = "SELECT * FROM FILMS AS ff " +
            "JOIN MPA AS m ON ff.mpa_id = m.mpa_id";
    public static final String SQL_QUERY_FIND_POPULAR_FILMS = "SELECT * FROM FILMS AS f " +
            "JOIN MPA AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN FILM_LIKES AS fl ON f.film_id = fl.film_id " +
            "GROUP BY f.FILM_ID, fl.USER_ID ORDER BY COUNT(user_id) DESC LIMIT ?";
    public static final String SQL_QUERY_FIND_FILM_BY_ID = "SELECT * FROM FILMS AS ff " +
            "JOIN MPA AS m ON ff.mpa_id = m.mpa_id WHERE film_id = ?";
    public static final String SQL_QUERY_ADD_FILM = "INSERT INTO FILMS" +
            "(name, description, release_date, duration, mpa_id) values (?, ?, ?, ?, ?)";
    public static final String SQL_QUERY_UPDATE_FILM = "UPDATE FILMS " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?";
    public static final String SQL_QUERY_DELETE_FILM = "DELETE FROM FILMS WHERE FILM_ID = ?";
    public static final String SQL_QUERY_CHECK_FILM = "SELECT COUNT(*) FROM FILMS WHERE film_id = ?";

    public static final String SQL_QUERY_FIND_COMMON_FILMS = "SELECT f.*, m.* FROM FILMS AS f " +
            "JOIN MPA AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN FILM_LIKES AS fl ON f.film_id = fl.film_id " +
            "WHERE f.film_id IN " +
            "(" +
            "    SELECT film_id FROM FILM_LIKES WHERE user_id = ? " +
            "    INTERSECT " +
            "    SELECT film_id FROM FILM_LIKES WHERE user_id = ? " +
            ")" +
            "GROUP BY f.film_id ORDER BY COUNT(fl.user_id) DESC";

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_FILMS, filmMapper);
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        return jdbcTemplate.query(SQL_QUERY_FIND_POPULAR_FILMS, filmMapper, count);
    }

    @Override
    public Film findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_QUERY_FIND_FILM_BY_ID, filmMapper, id);
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
        return film;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update(SQL_QUERY_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        return film;
    }

    @Override
    public void deleteFilm(Long id) {
        jdbcTemplate.update(SQL_QUERY_DELETE_FILM, id);
    }

    @Override
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(SQL_QUERY_CHECK_FILM, Integer.class, id);
        assert count != null;
        return count.equals(1);
    }

    @Override
    public List<Film> findCommonFilmsForUsers(Long userId, Long friendId) {
        return jdbcTemplate.query(SQL_QUERY_FIND_COMMON_FILMS, filmMapper, userId, friendId);
    }
}
