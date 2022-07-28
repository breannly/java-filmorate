package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


@Component
@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {
    private final MpaMapper mpaMapper;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDuration(rs.getLong("duration"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getObject("release_date", LocalDate.class));
        film.setRate(rs.getDouble("rate"));
        film.setMpa(mpaMapper.mapRow(rs, rowNum));

        return film;
    }
}
