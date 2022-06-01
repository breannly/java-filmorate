package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.generator.IdGenerator;
import ru.yandex.practicum.filmorate.model.IdEntity;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
            log.warn("Объект {} с id {} не найден", t, t.getId());
            throw new ValidationException("Обновление несуществующего объекта");
        }
        storage.put(t.getId(), t);
        log.info("Объект {} изменен", t);
        return t;
    }
}
