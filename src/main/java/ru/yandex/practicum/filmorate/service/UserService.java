package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.user.dao.FriendStorageDao;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorageDao;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorageDao userStorage;
    private final FriendStorageDao friendStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User add(User user) {
        validate(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        if (!userStorage.existsById(user.getId())) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
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

    public User findUserById(Long userId) {
        if (!userStorage.existsById(userId)) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        return userStorage.findById(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        if (!(userStorage.existsById(userId) && userStorage.existsById(friendId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (!(userStorage.existsById(userId) && userStorage.existsById(friendId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> findFriends(Long userId) {
        if (!userStorage.existsById(userId)) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        return friendStorage.findFriends(userId);
    }

    public List<User> findMutualFriends(Long userId, Long otherId) {
        if (!(userStorage.existsById(userId) && userStorage.existsById(otherId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        return friendStorage.findMutualFriends(userId, otherId);
    }
}
