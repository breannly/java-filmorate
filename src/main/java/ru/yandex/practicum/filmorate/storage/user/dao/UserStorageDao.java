package ru.yandex.practicum.filmorate.storage.user.dao;

import ru.yandex.practicum.filmorate.model.entity.Event;
import ru.yandex.practicum.filmorate.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserStorageDao {
    List<User> findAll();

    Optional<User> findById(Long id);

    User add(User user);

    User update(User user);

    void deleteUser(Long userId);

    boolean existsById(Long userId);

    List<Event> getFeed(Long userId);
}
