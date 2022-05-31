package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.generator.GeneratorId;
import ru.yandex.practicum.filmorate.model.Entity;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
abstract class Controller<T extends Entity> {
    private final Map<Long, T> storage;
    private final GeneratorId generator;

    public Controller() {
        storage = new HashMap<>();
        generator = new GeneratorId();
    }

    protected abstract void validate(T t) throws ValidationException;

    public Collection<T> findAll() {
        return storage.values();
    }

    public T add(@Valid @RequestBody T t) throws ValidationException {
        validate(t);

        t.setId(generator.generate());
        storage.put(t.getId(), t);
        log.info("Объект {} добавлен", t);

        return t;
    }

    public T update(@Valid @RequestBody T t) throws ValidationException {
        validate(t);

        if (!storage.containsKey(t.getId())) {
            log.warn("Объект с id: {} не найден", t.getId());
            throw new ValidationException("Обновление несуществующего объекта");
        }
        storage.put(t.getId(), t);
        log.info("Объект {} обновлен", t);

        return t;
    }
}
