package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.dao.FriendStorageDao;

import java.util.List;

import static ru.yandex.practicum.filmorate.config.Config.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorageDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long id, Long friendId) {
        if (isUserExists(id) && isUserExists(friendId)) {
            jdbcTemplate.update(SQL_QUERY_ADD_FRIEND, id, friendId);
            if (checkStatus(id, friendId)) {
                jdbcTemplate.update(SQL_QUERY_UPDATE_STATUS, 1, id, friendId);
                jdbcTemplate.update(SQL_QUERY_UPDATE_STATUS, 1, friendId, id);
            }
        } else throw new ObjectNotFoundException("Вызов несуществующего объекта");
    }

    private boolean checkStatus(Long userId, Long friendId) {
        List<FriendshipStatus> statuses = jdbcTemplate.query(SQL_QUERY_GET_STATUSES,
                (rs, rowNum) -> FriendshipStatus.findStatus(rs.getString("status")),
                userId, friendId, friendId, userId);
        return statuses.size() == 2;
    }

    private boolean isUserExists(Long userId) {
        int count = jdbcTemplate.queryForObject(SQL_QUERY_CHECK_USER, Integer.class, userId);
        return count > 0;
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        if (isUserExists(id) && isUserExists(friendId)) {
            jdbcTemplate.update(SQL_QUERY_DELETE_FRIEND, id, friendId);
            jdbcTemplate.update(SQL_QUERY_UPDATE_STATUS, 2, friendId, id);
        } else throw new ObjectNotFoundException("Вызов несуществующего объекта");
    }

    @Override
    public List<User> findFriends(Long id) {
        if (isUserExists(id)) {
            return jdbcTemplate.query(SQL_QUERY_FIND_FRIENDS, new UserMapper(), id);
        } else throw new ObjectNotFoundException("Вызов несуществующего объекта");
    }

    @Override
    public List<User> findMutualFriends(Long id, Long otherId) {
        if (isUserExists(id) && isUserExists(otherId)) {
            return jdbcTemplate.query(SQL_QUERY_FIND_MUTUAL_FRIENDS, new UserMapper(), id, otherId);
        }else throw new ObjectNotFoundException("Вызов несуществующего объекта");
    }
}