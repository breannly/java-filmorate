package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.generator.IdGenerator;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films;
    private final IdGenerator generator;

    @Autowired
    public InMemoryFilmStorage(IdGenerator generator) {
        films = new HashMap<>();
        this.generator = generator;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film add(Film film) {
        film.setId(generator.generate());
        films.put(film.getId(), film);
        log.info("Объект {} добавлен", film);

        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Объект {} с id {} не найден", film, film.getId());
            throw new ObjectNotFoundException("Обновление несуществующего объекта");
        }
        films.put(film.getId(), film);
        log.info("Объект {} изменен", film);

        return film;
    }

    @Override
    public Map<Long, Film> getFilms() {
        return new HashMap<>(films);
    }
}
