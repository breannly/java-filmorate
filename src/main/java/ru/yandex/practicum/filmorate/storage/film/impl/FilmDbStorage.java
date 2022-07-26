package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.config.FilmSearchStrategy;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.config.FilmSearchStrategy.*;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final NamedParameterJdbcTemplate namedParameter;

    private static final String SQL_QUERY_FIND_ALL_FILMS = "SELECT * FROM FILMS AS ff " +
            "JOIN MPA AS m ON ff.mpa_id = m.mpa_id";
    private static final String SQL_QUERY_FIND_FILM_BY_ID = "SELECT * FROM FILMS AS ff " +
            "JOIN MPA AS m ON ff.mpa_id = m.mpa_id WHERE film_id = ?";
    private static final String SQL_QUERY_ADD_FILM = "INSERT INTO FILMS" +
            "(name, description, release_date, duration, mpa_id) values (?, ?, ?, ?, ?)";
    private static final String SQL_QUERY_UPDATE_FILM = "UPDATE FILMS " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?";
    private static final String SQL_QUERY_DELETE_FILM = "DELETE FROM FILMS WHERE FILM_ID = ?";
    private static final String SQL_QUERY_CHECK_FILM = "SELECT COUNT(*) FROM FILMS WHERE film_id = ?";
    private static final String SQL_QUERY_FIND_COMMON_FILMS = "SELECT f.*, m.* FROM FILMS AS f " +
            "JOIN MPA AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN FILM_LIKES AS fl ON f.film_id = fl.film_id " +
            "WHERE f.film_id IN " +
            "(" +
            "    SELECT film_id FROM FILM_LIKES WHERE user_id = ? " +
            "    INTERSECT " +
            "    SELECT film_id FROM FILM_LIKES WHERE user_id = ? " +
            ")" +
            "GROUP BY f.film_id ORDER BY COUNT(fl.user_id) DESC";

    private static final String SQL_QUERY_FIND_FILMS_BY_DIRECTOR_SORT_BY_LIKES = "SELECT f.*, m.* FROM FILMS AS f " +
            "JOIN MPA AS m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN FILM_LIKES AS fl ON f.film_id = fl.film_id " +
            "WHERE f.film_id IN " +
            "(" +
            "    SELECT film_id FROM FILM_DIRECTORS WHERE director_id = ? " +
            ")" +
            "GROUP BY f.film_id ORDER BY COUNT(fl.user_id) DESC";

    private static final String SQL_QUERY_FIND_FILMS_BY_DIRECTOR_SORT_BY_YEAR = "SELECT f.*, m.* FROM FILMS AS f " +
            "JOIN MPA AS m ON f.mpa_id = m.mpa_id " +
            "WHERE f.film_id IN " +
            "(" +
            "    SELECT film_id FROM FILM_DIRECTORS WHERE director_id = ? " +
            ")" +
            "ORDER BY f.release_date ASC";

    private static final String SQL_QUERY_GET_RECOMMENDATION =
            "SELECT * FROM FILMS AS F " +
                    "JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                    "WHERE FILM_ID IN (" +
                    "    SELECT FILM_ID" +
                    "    FROM FILM_LIKES" +
                    "    WHERE USER_ID = (" +
                    "        SELECT TOP (1) USER_ID" +
                    "        FROM FILM_LIKES" +
                    "        WHERE FILM_ID IN" +
                    "              (SELECT FILM_ID FROM FILM_LIKES WHERE USER_ID = ?)" +
                    "          AND USER_ID != ?" +
                    "        GROUP BY USER_ID" +
                    "        ORDER BY COUNT(FILM_ID) DESC)" +
                    "      AND FILM_ID NOT IN (SELECT FILM_ID FROM FILM_LIKES WHERE USER_ID = ?))";

    private static final String SQL_QUERY_SEARCH_FILM = "SELECT * FROM FILMS f " +
            "JOIN MPA M ON M.MPA_ID = F.MPA_ID " +
            "LEFT JOIN FILM_LIKES AS fl ON fl.FILM_ID = f.FILM_ID " +
            "LEFT JOIN FILM_DIRECTORS fd ON fd.FILM_ID = f.FILM_ID " +
            "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID ";

    private static final String SQL_QUERY_SEARCH_POPULAR_FILM = SQL_QUERY_SEARCH_FILM +
            "GROUP BY F.FILM_ID, FL.USER_ID ORDER BY COUNT(DISTINCT FL.USER_ID) DESC LIMIT :count";

    private static final String SQL_QUERY_SEARCH_BY_YEAR_AND_GENRE = SQL_QUERY_SEARCH_FILM +
            "LEFT JOIN FILM_GENRES FG ON FG.FILM_ID = F.FILM_ID " +
            "WHERE FG.GENRE_ID = :genreId AND YEAR(F.RELEASE_DATE) = :year " +
            "GROUP BY F.FILM_ID ORDER BY COUNT(DISTINCT FL.USER_ID) DESC LIMIT :count";

    private static final String SQL_QUERY_SEARCH_BY_YEAR = SQL_QUERY_SEARCH_FILM +
            "WHERE YEAR(F.RELEASE_DATE) = :year " +
            "GROUP BY F.FILM_ID ORDER BY COUNT(DISTINCT FL.USER_ID) DESC LIMIT :count";

    private static final String SQL_QUERY_SEARCH_BY_GENRE = SQL_QUERY_SEARCH_FILM +
            "LEFT JOIN FILM_GENRES FG ON FG.FILM_ID = F.FILM_ID " +
            "WHERE FG.GENRE_ID  = :genreId "+
            "GROUP BY F.FILM_ID ORDER BY COUNT(DISTINCT FL.USER_ID) DESC LIMIT :count";

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_FILMS, filmMapper);
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

    @Override
    public List<Film> getRecommendations(Long userId) {
        return jdbcTemplate.query(SQL_QUERY_GET_RECOMMENDATION, filmMapper, userId, userId, userId);
    }

    @Override
    public List<Film> findFilmsByDirector(Long directorId, String sortBy) {
        String query = sortBy.equalsIgnoreCase("year") ? SQL_QUERY_FIND_FILMS_BY_DIRECTOR_SORT_BY_YEAR
                : SQL_QUERY_FIND_FILMS_BY_DIRECTOR_SORT_BY_LIKES;
        return jdbcTemplate.query(query, filmMapper, directorId);
    }

    @Override
    public List<Film> findPopularFilms(int count, Long genreId, int year) {
        MapSqlParameterSource params = new MapSqlParameterSource("count", count);
        FilmSearchStrategy strategy = getStrategyByYearAndGenre(genreId, year);
        switch (strategy) {
            case YEAR_AND_GENRE:
                params.addValue("genreId", genreId).addValue("year", year);
                return namedParameter.query(SQL_QUERY_SEARCH_BY_YEAR_AND_GENRE, params, filmMapper);
            case GENRE:
                params.addValue("genreId", genreId);
                return namedParameter.query(SQL_QUERY_SEARCH_BY_GENRE, params, filmMapper);
            case YEAR:
                params.addValue("year", year);
                return namedParameter.query(SQL_QUERY_SEARCH_BY_YEAR, params, filmMapper);
            default:
                return namedParameter.query(SQL_QUERY_SEARCH_POPULAR_FILM, params, filmMapper);
        }
    }

    private FilmSearchStrategy getStrategyByYearAndGenre(Long genreId, int year) {
        if (genreId != null && year != 0) { return YEAR_AND_GENRE;}
        if (genreId != null) { return GENRE;}
        if (year != 0) { return YEAR;}
        return DEFAULT;
    }

    @Override
    public List<Film> searchFilmsByNameOrDirector(String textQuery, List<String> searchParams) {
        MapSqlParameterSource params = new MapSqlParameterSource
                ("textQuery", "%" + textQuery.toLowerCase() + "%");
        StringBuilder sqlQuery = new StringBuilder(SQL_QUERY_SEARCH_FILM);
        String groupBy = " GROUP BY f.FILM_ID ORDER BY COUNT(fl.USER_ID) DESC";
        FilmSearchStrategy strategy = getStrategyByTitleAndDirector(searchParams);
        switch (strategy) {
            case TITLE_AND_DIRECTOR:
                sqlQuery.append("WHERE (LOWER(f.NAME) LIKE :textQuery OR LOWER(d.NAME) LIKE :textQuery)")
                        .append(groupBy);
                break;
            case DIRECTOR:
                sqlQuery.append("WHERE LOWER(d.NAME) LIKE :textQuery").append(groupBy);
                break;
            case TITLE:
                sqlQuery.append("WHERE LOWER(f.NAME) LIKE :textQuery").append(groupBy);
                break;
        }
        return namedParameter.query(sqlQuery.toString(), params, filmMapper);
    }

    private FilmSearchStrategy getStrategyByTitleAndDirector(List<String> searchParams) {
        if (searchParams.size() == 2)
            return TITLE_AND_DIRECTOR;
        else if (searchParams.contains("director")) {
            return FilmSearchStrategy.DIRECTOR;
        }          return FilmSearchStrategy.TITLE;
    }
}