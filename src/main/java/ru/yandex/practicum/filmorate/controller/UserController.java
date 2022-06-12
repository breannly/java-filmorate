package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping
    public Collection<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        validate(user);
        return inMemoryUserStorage.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validate(user);
        return inMemoryUserStorage.update(user);
    }

    private void validate(User user) {
        boolean isWrongName = user.getName().isBlank();

        if (isWrongName) {
            log.info("У пользователя {} user изменено имя на {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }
}
