package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.generator.GeneratorUserId;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users;
    private final GeneratorUserId generatorId;

    public UserController() {
        users = new HashMap<>();
        generatorId = new GeneratorUserId();
    }

    @GetMapping
    public Collection<User> getUsers(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());

        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        validate(user);

        users.put(createUser(user), user);
        return user;
    }

    private Long createUser(User user) {
        user.setId(generatorId.generate());

        return user.getId();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}'", request.getMethod(), request.getRequestURI());
        validate(user);

        if (!users.containsKey(user.getId())) {
            log.info("Неверно указан id");
            throw new ValidationException("Пользователя с таким id нет");
        }
        users.put(user.getId(), user);

        return user;
    }

    private void validate(User user) throws ValidationException {
        boolean isWrongLogin = user.getLogin().contains(" ");
        boolean isWrongName = user.getName().isBlank();

        if (isWrongLogin) {
            log.info("Логин содержит пробелы");
            throw new ValidationException("Пользователь не соответствует критериям");
        }

        if (isWrongName) {
            log.info("Имя изменено на логин");
            user.setName(user.getLogin());
        }
    }
}
