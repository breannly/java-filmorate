package ru.yandex.practicum.filmorate.storage.user.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.generator.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users;
    private final IdGenerator generator;

    @Autowired
    public InMemoryUserStorage(IdGenerator generator) {
        users = new HashMap<>();
        this.generator = generator;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User add(User user) {
        user.setId(generator.generate());
        users.put(user.getId(), user);
        log.info("Объект {} добавлен", user);

        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Объект {} с id {} не найден", user, user.getId());
            throw new ObjectNotFoundException("Обновление несуществующего объекта");
        }
        users.put(user.getId(), user);
        log.info("Объект {} изменен", user);

        return user;
    }

    @Override
    public Map<Long, User> getUsers() {
        return new HashMap<>(users);
    }
}
