package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.storage.film.dao.DirectorStorageDao;
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
    private final DirectorStorageDao directorStorage;

    public List<Film> findAll() {
        log.info("Получение списка всех фильмов");
        List<Film> films = filmStorage.findAll();
        films.forEach(film -> {
            film.setGenres(genreStorage.findAllById(film.getId()));
            film.setDirectors(directorStorage.findAllById(film.getId()));
        });
        return films;
    }

    public Film add(Film film) {
        validate(film);
        filmStorage.add(film);
        film.setGenres(genreStorage.addToFilm(film.getId(), film.getGenres()));
        film.setDirectors(directorStorage.addToFilm(film.getId(), film.getDirectors()));
        log.info("Добавление нового фильма c id {}", film.getId());

        return film;
    }

    public Film update(Film film) {
        validate(film);
        if (!filmStorage.existsById(film.getId())) {
            log.warn("Фильм с id {} не найден", film.getId());
            throw new ObjectNotFoundException("Фильм не найден");
        }
        filmStorage.update(film);
        genreStorage.addToFilm(film.getId(), film.getGenres());
        if (film.getGenres() != null) film.setGenres(genreStorage.findAllById(film.getId()));
        directorStorage.addToFilm(film.getId(), film.getDirectors());
        if (film.getDirectors() != null) film.setDirectors(directorStorage.findAllById(film.getId()));
        log.info("Обновление фильма с id {}", film.getId());

        return film;
    }

    public void deleteFilm(Long filmId) {
        if (!filmStorage.existsById(filmId)) {
            log.warn("Фильм с id {} не найден", filmId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Удаление фильма с id {}", filmId);
        filmStorage.deleteFilm(filmId);
    }

    private void validate(Film film) {
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);

        if (isWrongReleaseDate) {
            log.warn("Слишком раняя дата релиза");
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }

    public Film findFilmById(Long filmId) {
        if (!filmStorage.existsById(filmId)) {
            log.warn("Фильм с id {} не найден", filmId);
            throw new ObjectNotFoundException("Фильм не найден");
        }
        Film foundFilm = filmStorage.findById(filmId);
        foundFilm.setGenres(genreStorage.findAllById(filmId));
        foundFilm.setDirectors(directorStorage.findAllById(filmId));
        log.info("Получение фильма с id {}", filmId);
        return foundFilm;
    }

    public List<Film> findPopularFilms(int count, Long genreId, int year) {
        log.info("Получение {} фильмов", count);
        if (year < 0 || count < 0)
            throw new ValidationException("В параметрах запроса не должно быть отрицательных чисел.");
        if (genreId != 0 && (!genreStorage.existsById(genreId)))
            throw new ValidationException("Такого жанра нет");
        List<Film> popularFilms = filmStorage.findPopularFilms(count, genreId, year);
        popularFilms.forEach(film -> {
            film.setGenres(genreStorage.findAllById(film.getId()));
            film.setDirectors(directorStorage.findAllById(film.getId()));
        });
        return popularFilms;
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

    public List<Film> findCommonFilms(Long userId, Long friendId) {
        if (!(userStorage.existsById(userId) && userStorage.existsById(friendId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Получение общих фильмов для пользователя {} и {}", userId, friendId);
        return filmStorage.findCommonFilmsForUsers(userId, friendId);
    }

    public List<Film> findFilmsByDirector(Long directorId, String sortBy) {
        if (!(directorStorage.existsById(directorId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Получение фильмов режиссера {} отсортированных по {}", directorId, sortBy);
        List<Film> films = filmStorage.findFilmsByDirector(directorId, sortBy);
        films.forEach(film -> {
            film.setGenres(genreStorage.findAllById(film.getId()));
            film.setDirectors(directorStorage.findAllById(film.getId()));
        });
        return films;
    }
}
