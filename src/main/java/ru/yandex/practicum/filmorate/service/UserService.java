package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public Collection<User> findAll() {
        return storage.findAll();
    }

    public User add(User user) {
        return storage.add(user);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public void addFriend(Long id, Long friendId) {
        User user = findUserById(id);
        User userFriend  = findUserById(friendId);

        user.getFriends().add(friendId);
        userFriend.getFriends().add(id);
    }

    public User findUserById(Long id) {
        User user = storage.getUsers().get(id);
        if (user == null) {
            log.warn("Пользователь {} не найден", id);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }

        return user;
    }

    public void deleteFriend(Long id, Long friendId) {
        User user = findUserById(id);
        User userFriend  = findUserById(friendId);
        log.info("Удаление из друзей {} {}", id, friendId);

        user.getFriends().remove(friendId);
        userFriend.getFriends().remove(id);
    }

    public List<User> getFriends(Long id) {
        User user = findUserById(id);
        log.info("Получение списка друзей {}", id);

        return user.getFriends().stream()
                .map(this::findUserById).
                collect(Collectors.toList());
    }

    public List<User> getMutualFriends(Long id, Long otherId) {
        // используется метод двух указателей, сложность алгоритма O(n)
        List<User> mutualFriends = new ArrayList<>();
        List<Long> idFriends = new ArrayList<>(findUserById(id).getFriends());
        List<Long> otherIdFriends = new ArrayList<>(findUserById(otherId).getFriends());

        int fPointer = 0;
        int sPointer = 0;
        while (fPointer < idFriends.size() && sPointer < otherIdFriends.size()) {
            if (idFriends.get(fPointer) < otherIdFriends.get(sPointer)) {
                fPointer++;
            } else if (idFriends.get(fPointer) > otherIdFriends.get(sPointer)) {
                sPointer++;
            } else {
                mutualFriends.add(findUserById(idFriends.get(fPointer)));
                fPointer++;
                sPointer++;
            }
        }
        log.info("Вывод общих друзей {} и {}", id, otherId);

        return mutualFriends;
    }
}
