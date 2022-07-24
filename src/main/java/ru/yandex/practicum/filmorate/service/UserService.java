package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.Event;
import ru.yandex.practicum.filmorate.model.entity.EventType;
import ru.yandex.practicum.filmorate.model.entity.OperationType;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.user.dao.EventStorageDao;
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
    private final EventStorageDao eventStorage;

    public Collection<User> findAll() {
        log.info("Получение списка всех пользователей");
        return userStorage.findAll();
    }

    public User add(User user) {
        validate(user);
        User addedUser = userStorage.add(user);
        log.info("Добавление нового пользователя с id {}", addedUser.getId());

        return addedUser;
    }

    public User update(User user) {
        if (!userStorage.existsById(user.getId())) {
            log.warn("Пользователь с id {} не найден", user.getId());
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        validate(user);
        log.info("Обновление пользователя с id {}", user.getId());
        return userStorage.update(user);
    }

    public void deleteUser(Long userId) {
        if (!userStorage.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Удаление пользователя с id {}", userId);
        userStorage.deleteUser(userId);
    }

    private void validate(User user) {
        boolean isWrongName = user.getName().isBlank();

        if (isWrongName) {
            log.warn("У пользователя {} user изменено имя на {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }

    public User findUserById(Long userId) {
        if (!userStorage.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Получение пользователя с id {}", userId);
        return userStorage.findById(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        if (!(userStorage.existsById(userId) && userStorage.existsById(friendId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Пользователь {} добавил {}", userId, friendId);
        eventStorage.add(userId, EventType.FRIEND, OperationType.ADD, friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (!(userStorage.existsById(userId) && userStorage.existsById(friendId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Пользователь {} удалил {}", userId, friendId);
        eventStorage.add(userId, EventType.FRIEND, OperationType.REMOVE, friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> findFriends(Long userId) {
        if (!userStorage.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Получение друзей пользователя с id {}", userId);
        return friendStorage.findFriends(userId);
    }

    public List<User> findMutualFriends(Long userId, Long otherId) {
        if (!(userStorage.existsById(userId) && userStorage.existsById(otherId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Получение общих друзей пользователей с id {} и {}", userId, otherId);
        return friendStorage.findMutualFriends(userId, otherId);
    }

    public List<Event> getFeed(Long userId) {
        if (!userStorage.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Получение ленты пользователя с id {}", userId);
        return userStorage.getFeed(userId);
    }
}
