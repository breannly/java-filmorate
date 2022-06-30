package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    public Collection<User> findAll();

    public User add(User user);

    public User update(User user);

    public Map<Long, User> getUsers();
}
