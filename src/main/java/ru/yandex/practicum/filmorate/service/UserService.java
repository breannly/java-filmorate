package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.user.dao.FriendStorageDao;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorageDao;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorageDao userStorage;
    private final FriendStorageDao friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorageDao userStorage, FriendStorageDao friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User add(User user) {
        validate(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        validate(user);
        return userStorage.update(user);
    }

    private void validate(User user) {
        boolean isWrongName = user.getName().isBlank();

        if (isWrongName) {
            log.info("У пользователя {} user изменено имя на {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }

    public User findUserById(Long id) {
        return userStorage.findUserById(id);
    }

    public void addFriend(Long id, Long friendId) {
        friendStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        friendStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(Long id) {
        return friendStorage.findFriends(id);
    }

    public List<User> getMutualFriends(Long id, Long otherId) {
        return friendStorage.findMutualFriends(id, otherId);
    }
}
