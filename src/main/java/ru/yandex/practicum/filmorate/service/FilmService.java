package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;

import java.util.List;

import static ru.yandex.practicum.filmorate.config.Config.validateDate;

@Slf4j
@Service
public class FilmService {
    private final FilmStorageDao storage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorageDao storage) {
        this.storage = storage;
    }

    public List<Film> findAll() {
        return storage.findAll();
    }

    public Film add(Film film) {
        validate(film);
        return storage.add(film);
    }

    public Film update(Film film) {
        validate(film);
        return storage.update(film);
    }

    private void validate(Film film) {
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);

        if (isWrongReleaseDate) {
            log.warn("Слишком раняя дата релиза");
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }

    public Film findFilmById(Long id) {
        Film film = storage.findFilmById(id);
        if (film == null) {
            log.warn("Фильм {} не найден", id);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }

        return film;
    }

    public List<Film> getFilms(int count) {
        log.info("Получение {} фильмов", count);
        return storage.findFilms(count);
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
}
