package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public Collection<Film> findAll() {
        return storage.findAll();
    }

    public Film add(Film film) {
        return storage.add(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public Film findFilmById(Long id) {
        Film film = storage.getFilms().get(id);
        if (film == null) {
            log.warn("Фильм {} не найден", id);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }

        return film;
    }

    public void addLike(Long id, Long filmId) {
        Film film = findFilmById(filmId);
        film.getLikes().add(id);
        log.info("Пользователь {} поставил лайк фильму {}", id, filmId);
    }

    public void deleteLike(Long id, Long filmId) {
        Film film = findFilmById(filmId);
        film.getLikes().remove(id);
        log.info("Пользователь {} удалил лайк у фильма {}", id, filmId);
    }

    public List<Film> getFilms(int count) {
        log.info("Получение {} фильмов", count);
        return storage.getFilms().values()
                .stream()
                .sorted((x, y) -> x.getLikes().size() - y.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
