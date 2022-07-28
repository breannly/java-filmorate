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
    private final ValidationService validationService;

    public Collection<User> findAll() {
        log.info("Получение списка всех пользователей");
        return userStorage.findAll();
    }

    public User add(User user) {
        validationService.validateUser(user);
        User addedUser = userStorage.add(user);
        log.info("Добавление нового пользователя с id {}", addedUser.getId());

        return addedUser;
    }

    public User update(User user) {
        validationService.checkExistsUser(user.getId());
        validationService.validateUser(user);

        log.info("Обновление пользователя с id {}", user.getId());
        return userStorage.update(user);
    }

    public void deleteUser(Long userId) {
        validationService.checkExistsUser(userId);

        log.info("Удаление пользователя с id {}", userId);
        userStorage.deleteUser(userId);
    }

    public User findUserById(Long userId) {
        validationService.checkExistsUser(userId);

        log.info("Получение пользователя с id {}", userId);
        return userStorage.findById(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        validationService.checkExistsUser(userId);
        validationService.checkExistsUser(friendId);

        log.info("Пользователь {} добавил {}", userId, friendId);
        eventStorage.add(userId, EventType.FRIEND, OperationType.ADD, friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        validationService.checkExistsUser(userId);
        validationService.checkExistsUser(friendId);

        log.info("Пользователь {} удалил {}", userId, friendId);
        eventStorage.add(userId, EventType.FRIEND, OperationType.REMOVE, friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> findFriends(Long userId) {
        validationService.checkExistsUser(userId);

        log.info("Получение друзей пользователя с id {}", userId);
        return friendStorage.findFriends(userId);
    }

    public List<User> findMutualFriends(Long userId, Long otherId) {
        validationService.checkExistsUser(userId);
        validationService.checkExistsUser(otherId);

        log.info("Получение общих друзей пользователей с id {} и {}", userId, otherId);
        return friendStorage.findMutualFriends(userId, otherId);
    }

    public List<Event> getFeed(Long userId) {
        validationService.checkExistsUser(userId);

        log.info("Получение ленты пользователя с id {}", userId);
        return userStorage.getFeed(userId);
    }

}
