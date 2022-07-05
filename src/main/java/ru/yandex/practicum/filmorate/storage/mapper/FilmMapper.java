package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film(
                rs.getString("name"),
                rs.getString("description"),
                rs.getObject("release_date", LocalDate.class),
                rs.getLong("duration"));
        film.setId(rs.getLong("film_id"));

        return film;
    }
}
