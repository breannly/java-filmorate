package ru.yandex.practicum.filmorate.storage.user.dao;

import ru.yandex.practicum.filmorate.model.entity.User;

    import java.util.List;

public interface UserStorageDao {
    public List<User> findAll();

    public User findUserById(Long id);

    public User add(User user);

    public User update(User user);

}
