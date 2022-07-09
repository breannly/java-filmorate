package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.dao.FriendStorageDao;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorageDao {
    private final JdbcTemplate jdbcTemplate;

    public static final String SQL_QUERY_UPDATE_STATUS = "UPDATE USER_FRIENDS SET status_id = ? " +
            "WHERE user_id = ? AND friend_id = ?";
    public static final String SQL_QUERY_ADD_FRIEND = "MERGE INTO USER_FRIENDS (user_id, friend_id) VALUES (?, ?)";
    public static final String SQL_QUERY_GET_STATUSES = "SELECT status FROM USER_FRIENDS AS uf " +
            "JOIN FRIENDSHIP_STATUSES AS fs ON uf.status_id = fs.status_id " +
            "WHERE user_id = ? AND friend_id = ? OR user_id = ? AND friend_id = ?";
    public static final String SQL_QUERY_DELETE_FRIEND = "DELETE FROM USER_FRIENDS WHERE user_id = ? AND friend_id = ?";
    public static final String SQL_QUERY_FIND_FRIENDS = "SELECT * FROM USERS AS fu " +
            "WHERE EXISTS (SELECT * FROM USER_FRIENDS WHERE user_id = ? AND friend_id = fu.id)";
    public static final String SQL_QUERY_FIND_MUTUAL_FRIENDS = "SELECT * FROM USERS " +
            "WHERE EXISTS " +
            "(SELECT * FROM user_friends WHERE USER_FRIENDS.user_id = ? " +
            "AND USER_FRIENDS.friend_id = USERS.id ) " +
            "AND EXISTS" +
            "(SELECT * FROM USER_FRIENDS WHERE user_friends.user_id = ? " +
            "AND USER_FRIENDS.friend_id = USERS.id )";

    @Override
    public void addFriend(Long id, Long friendId) {
        jdbcTemplate.update(SQL_QUERY_ADD_FRIEND, id, friendId);
        if (checkStatus(id, friendId)) {
            jdbcTemplate.update(SQL_QUERY_UPDATE_STATUS, 1, id, friendId);
            jdbcTemplate.update(SQL_QUERY_UPDATE_STATUS, 1, friendId, id);
        }
    }

    private boolean checkStatus(Long userId, Long friendId) {
        List<FriendshipStatus> statuses = jdbcTemplate.query(SQL_QUERY_GET_STATUSES,
                (rs, rowNum) -> FriendshipStatus.findStatus(rs.getString("status")),
                userId, friendId, friendId, userId);
        return statuses.size() == 2;
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_FRIEND, id, friendId);
        jdbcTemplate.update(SQL_QUERY_UPDATE_STATUS, 2, friendId, id);
    }

    @Override
    public List<User> findFriends(Long id) {
        return jdbcTemplate.query(SQL_QUERY_FIND_FRIENDS, new UserMapper(), id);
    }

    @Override
    public List<User> findMutualFriends(Long id, Long otherId) {
        return jdbcTemplate.query(SQL_QUERY_FIND_MUTUAL_FRIENDS, new UserMapper(), id, otherId);
    }
}