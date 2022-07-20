package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorageDao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public static final String SQL_QUERY_FIND_ALL_USERS = "SELECT * FROM USERS";
    public static final String SQL_QUERY_FIND_USER_BY_ID = "SELECT * FROM USERS WHERE USER_ID = ?";
    public static final String SQL_QUERY_ADD_USER = "INSERT INTO USERS(email, login, name, birthday) " +
            "values (?, ?, ?, ?)";
    public static final String SQL_QUERY_UPDATE_USER = "UPDATE USERS " +
            "SET email = ?, login = ?, name = ?, birthday = ? WHERE USER_ID = ?";
    public static final String SQL_QUERY_DELETE_USER = "DELETE FROM USERS WHERE USER_ID = ?";
    private static final String SQL_QUERY_CHECK_USER = "SELECT COUNT(*) FROM USERS WHERE USER_ID = ?";

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_USERS, userMapper);
    }

    @Override
    public User findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_QUERY_FIND_USER_BY_ID, userMapper, id);
    }

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_QUERY_ADD_USER, new String[]{"user_id"});
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
        jdbcTemplate.update(SQL_QUERY_UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        jdbcTemplate.update(SQL_QUERY_DELETE_USER, id);
    }

    @Override
    public boolean existsById(Long userId) {
        Integer count = jdbcTemplate.queryForObject(SQL_QUERY_CHECK_USER, Integer.class, userId);
        assert count != null;
        return count.equals(1);
    }
}
