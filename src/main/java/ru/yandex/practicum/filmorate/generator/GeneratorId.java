package ru.yandex.practicum.filmorate.generator;

class GeneratorId {
    private Long id;

    public GeneratorId() {
        id = 0L;
    }

    public Long generate() {
        id += 1;

        return id;
    }
}
