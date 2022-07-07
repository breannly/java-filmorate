package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorageDao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.config.Config.*;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorageDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_USERS, new UserMapper());
    }

    @Override
    public User findUserById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SQL_QUERY_FIND_USER_BY_ID, new UserMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_QUERY_ADD_USER, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setObject(4, user.getBirthday());
            return stmt;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        int count = jdbcTemplate.update(SQL_QUERY_UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        if (count > 0) {
            return user;
        } else throw new ObjectNotFoundException("Вызов несуществующего объекта");
    }
}
