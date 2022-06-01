package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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
    public User add(@RequestBody User user) throws ValidationException {
        return super.add(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        return super.update(user);
    }

    @Override
    protected void validate(User user) throws ValidationException {
        boolean isWrongName = user.getName().isBlank();
        boolean isWrongBirthday = user.getBirthday().isAfter(LocalDate.now());
        boolean isWrongLogin = user.getLogin().isBlank() || user.getLogin().contains(" ");
        boolean isWrongEmail = user.getEmail().isBlank() || !user.getEmail().contains("@");

        if (isWrongName) {
            log.info("У пользователя {} user изменено имя на {}", user, user.getLogin());
            user.setName(user.getLogin());
        }

        if (isWrongBirthday) {
            log.warn("Дата рождения не может быть в будущем");
            throw new ValidationException("День рождения в будущем");
        }

        if (isWrongLogin) {
            log.warn("Неподходящий логин");
            throw new ValidationException("Неподходящий логин");
        }

        if (isWrongEmail) {
            log.warn("Недействующий email");
            throw new ValidationException("Недействующий email");
        }
    }
}
