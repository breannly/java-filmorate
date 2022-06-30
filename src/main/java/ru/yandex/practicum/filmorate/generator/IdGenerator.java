package ru.yandex.practicum.filmorate.generator;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IdGenerator {
    private Long id;

    public IdGenerator() {
        id = 0L;
    }

    public Long generate() {
        id++;

        return id;
    }
}
