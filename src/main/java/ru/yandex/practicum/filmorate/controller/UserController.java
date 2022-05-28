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
public class UserController extends Controller<User> {
    private final GeneratorUserId generatorId;

    public UserController() {
        generatorId = new GeneratorUserId();
    }

    @GetMapping
    public Collection<User> findAll() {
        return super.findAll();
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) throws ValidationException {
        validate(user);

        return super.add(createUser(user));
    }

    private User createUser(User user) {
        user.setId(generatorId.generate());

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        validate(user);

        return super.update(user);
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
