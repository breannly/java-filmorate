package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorageDao;

import java.util.List;

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
            return jdbcTemplate.queryForObject(SQL_QUERY_FIND_USER, new UserMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }

    @Override
    public User add(User user) {
        jdbcTemplate.update(SQL_QUERY_ADD_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        return user;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(SQL_QUERY_UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }
}
