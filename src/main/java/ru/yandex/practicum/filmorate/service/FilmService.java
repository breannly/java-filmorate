package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.LikeStorageDao;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorageDao;

import java.util.List;

import static ru.yandex.practicum.filmorate.config.Config.validateDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorageDao filmStorage;
    private final UserStorageDao userStorage;
    private final LikeStorageDao likeStorage;
    private final GenreStorageDao genreStorage;

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film add(Film film) {
        validate(film);
        filmStorage.add(film);
        genreStorage.add(film.getId(), film.getGenres());

        return findFilmById(film.getId());
    }

    public Film update(Film film) {
        validate(film);
        if (!filmStorage.existsById(film.getId())) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
        filmStorage.update(film);
        genreStorage.add(film.getId(), film.getGenres());

        return findFilmById(film.getId());
    }

    private void validate(Film film) {
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);

        if (isWrongReleaseDate) {
            log.warn("Слишком раняя дата релиза");
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }

    public Film findFilmById(Long id) {
        if (!filmStorage.existsById(id)) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
        Film foundFilm = filmStorage.findById(id);
        foundFilm.setGenres(genreStorage.findAllById(id));
        return foundFilm;
    }

    public List<Film> getFilms(int count) {
        log.info("Получение {} фильмов", count);
        return filmStorage.findFilms(count);
    }

    public void addLike(Long filmId, Long userId) {
        if (!(filmStorage.existsById(filmId) && userStorage.existsById(userId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        if (!(filmStorage.existsById(filmId) && userStorage.existsById(userId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
        likeStorage.deleteLike(filmId, userId);
    }
}
