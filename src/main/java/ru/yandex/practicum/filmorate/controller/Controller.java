package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Entity;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
abstract class Controller<T> {
    private final Map<Long, T> storage;

    public Controller() {
        storage = new HashMap<>();
    }

    public Collection<T> findAll() {
        return storage.values();
    }

    public T add(@Valid @RequestBody T t) throws ValidationException {
        if (t instanceof Entity) {
            storage.put(((Entity) t).getId(), t);
        }

        return t;
    }

    public T update(@Valid @RequestBody T t) throws ValidationException {
        if (t instanceof Entity) {
            if (!storage.containsKey(((Entity) t).getId())) {
                log.info("Неверно указан id");
                throw new ValidationException("Пользователя с таким id нет");
            }
            storage.put(((Entity) t).getId(), t);
        }

        return t;
    }
}
