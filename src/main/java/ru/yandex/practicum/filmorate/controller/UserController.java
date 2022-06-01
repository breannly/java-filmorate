package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    @GetMapping
    public Collection<User> findAll() {
        return super.findAll();
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) throws ValidationException {
        return super.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        return super.update(user);
    }

    @Override
    protected void validate(User user) {
        boolean isWrongName = user.getName().isBlank();

        if (isWrongName) {
            log.info("У пользователя {} user изменено имя на {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }
}
