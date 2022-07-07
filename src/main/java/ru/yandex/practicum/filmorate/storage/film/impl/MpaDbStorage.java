package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.model.entity.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaStorageDao;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;

import java.util.List;

import static ru.yandex.practicum.filmorate.config.Config.*;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_MPA, mpaMapper);
    }

    @Override
    public Mpa findMpaById(Long mpaId) {
        try {
            return jdbcTemplate.queryForObject(SQL_QUERY_FIND_MPA_BY_ID, mpaMapper, mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }
}
