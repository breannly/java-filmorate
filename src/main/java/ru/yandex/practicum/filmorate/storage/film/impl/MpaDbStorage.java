package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.entity.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaStorageDao;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    public static final String SQL_QUERY_FIND_ALL_MPA = "SELECT * FROM MPA";
    public static final String SQL_QUERY_FIND_MPA_BY_ID = "SELECT * FROM MPA WHERE mpa_id = ?";
    private static final String SQL_QUERY_CHECK_MPA = "SELECT COUNT(*) FROM MPA WHERE mpa_id = ?";

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_MPA, mpaMapper);
    }

    @Override
    public Optional<Mpa> findById(Long mpaId) {
        List<Mpa> mpa = jdbcTemplate.query(SQL_QUERY_FIND_MPA_BY_ID, mpaMapper, mpaId);
        if (mpa.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mpa.get(0));
    }

    @Override
    public boolean existsById(Long mpaId) {
        Integer count = jdbcTemplate.queryForObject(SQL_QUERY_CHECK_MPA, Integer.class, mpaId);
        assert count != null;
        return count.equals(1);
    }
}
