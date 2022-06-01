package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.generator.IdGenerator;
import ru.yandex.practicum.filmorate.model.IdEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractController<T extends IdEntity> {
    private final Map<Long, T> storage;
    private final IdGenerator generator;

    public AbstractController() {
        storage = new HashMap<>();
        generator = new IdGenerator();
    }

    protected abstract void validate(T t) throws ValidationException;

    public Collection<T> findAll() {
        return storage.values();
    }

    public T add(@RequestBody T t) throws ValidationException {
        validate(t);

        t.setId(generator.generate());
        storage.put(t.getId(), t);

        return t;
    }

    public T update(@RequestBody T t) throws ValidationException {
        validate(t);

        if (!storage.containsKey(t.getId())) {
            throw new ValidationException("Обновление несуществующего объекта");
        }
        storage.put(t.getId(), t);

        return t;
    }
}
