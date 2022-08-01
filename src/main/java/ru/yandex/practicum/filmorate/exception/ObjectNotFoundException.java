package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class ObjectNotFoundException extends RuntimeException {
    private final Long id;
    private final String clazz;

    public ObjectNotFoundException(Long id, String clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    @Override
    public String getMessage() {
        return "Вызов несуществующего " + clazz + " c id = " + id;
    }
}
