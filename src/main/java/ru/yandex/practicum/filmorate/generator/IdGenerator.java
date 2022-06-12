package ru.yandex.practicum.filmorate.generator;

abstract class IdGenerator {
    private Long id;

    public IdGenerator() {
        id = 0L;
    }

    public Long generate() {
        id++;

        return id;
    }
}
