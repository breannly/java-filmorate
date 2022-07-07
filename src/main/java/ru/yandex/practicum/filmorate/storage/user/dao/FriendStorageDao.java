package ru.yandex.practicum.filmorate.storage.user.dao;

import ru.yandex.practicum.filmorate.model.entity.User;

import java.util.List;

public interface FriendStorageDao {
    public void addFriend(Long id, Long friendId);

    public void deleteFriend(Long id, Long friendId);

    public List<User> findFriends(Long id);

    public List<User> findMutualFriends(Long id, Long otherId);
}