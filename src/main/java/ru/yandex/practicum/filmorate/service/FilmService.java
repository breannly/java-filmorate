package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.LikeStorageDao;

import java.util.List;

import static ru.yandex.practicum.filmorate.config.Config.validateDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorageDao filmStorage;
    private final LikeStorageDao likeStorage;

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film add(Film film) {
        validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validate(film);
        return filmStorage.update(film);
    }

    private void validate(Film film) {
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);

        if (isWrongReleaseDate) {
            log.warn("Слишком раняя дата релиза");
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }

    public Film findFilmById(Long id) {
        return filmStorage.findFilmById(id);
    }

    public List<Film> getFilms(int count) {
        log.info("Получение {} фильмов", count);
        return filmStorage.findFilms(count);
    }

    public void addLike(Long id, Long userId) {
        log.info("Пользователь {} поставил лайк фильму {}", id, userId);
        likeStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long filmId) {
        log.info("Пользователь {} удалил лайк у фильма {}", id, filmId);
        likeStorage.deleteLike(id, filmId);
    }
}
