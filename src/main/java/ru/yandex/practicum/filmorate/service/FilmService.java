package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.entity.EventType;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.OperationType;
import ru.yandex.practicum.filmorate.storage.film.dao.DirectorStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.LikeStorageDao;
import ru.yandex.practicum.filmorate.storage.user.dao.EventStorageDao;
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
    private final EventStorageDao eventStorage;
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
        checkExistsFilm(film.getId());

        filmStorage.update(film);
        genreStorage.addToFilm(film.getId(), film.getGenres());
        if (film.getGenres() != null) film.setGenres(genreStorage.findAllById(film.getId()));
        directorStorage.addToFilm(film.getId(), film.getDirectors());
        if (film.getDirectors() != null) film.setDirectors(directorStorage.findAllById(film.getId()));
        log.info("Обновление фильма с id {}", film.getId());

        return film;
    }

    public void deleteFilm(Long filmId) {
        checkExistsFilm(filmId);

        log.info("Удаление фильма с id {}", filmId);
        filmStorage.deleteFilm(filmId);
    }

    private void validate(Film film) {
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);

        if (isWrongReleaseDate) {
            log.warn("Слишком ранняя дата релиза");
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }

    public Film findFilmById(Long filmId) {
        checkExistsFilm(filmId);

        Film foundFilm = filmStorage.findById(filmId);
        foundFilm.setGenres(genreStorage.findAllById(filmId));
        foundFilm.setDirectors(directorStorage.findAllById(filmId));
        log.info("Получение фильма с id {}", filmId);
        return foundFilm;
    }

    public List<Film> findPopularFilms(int count, Long genreId, int year) {
        if (genreId != null && (!genreStorage.existsById(genreId)))
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        List<Film> popularFilms = filmStorage.findPopularFilms(count, genreId, year);
        popularFilms.forEach(film -> {
            film.setGenres(genreStorage.findAllById(film.getId()));
            film.setDirectors(directorStorage.findAllById(film.getId()));
        });
        log.info("Получение {} фильмов", count);
        return popularFilms;
    }

    public void addLike(Long filmId, Long userId) {
        checkExistsFilm(filmId);
        checkExistsUser(userId);

        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
        likeStorage.addLike(filmId, userId);
        eventStorage.add(userId, EventType.LIKE, OperationType.ADD, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        checkExistsFilm(filmId);
        checkExistsUser(userId);

        log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
        likeStorage.deleteLike(filmId, userId);
        eventStorage.add(userId, EventType.LIKE, OperationType.REMOVE, filmId);
    }

    public List<Film> findCommonFilms(Long userId, Long friendId) {
        checkExistsUser(userId);
        checkExistsUser(friendId);

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

    public List<Film> getRecommendations(Long userId) {
        checkExistsUser(userId);

        List<Film> recommendationsFilms = filmStorage.getRecommendations(userId);
        recommendationsFilms.forEach(f -> {
            f.setGenres(genreStorage.findAllById(f.getId()));
            f.setDirectors(directorStorage.findAllById(f.getId()));
        });
        return recommendationsFilms;
    }

    public List<Film> searchFilms(String textQuery, List<String> searchParams) {
        List<Film> searchResult = filmStorage.searchFilmsByNameOrDirector(textQuery, searchParams);
        log.info("Поиск фильма по запросу {} ", textQuery);
        searchResult.forEach(film -> {
            film.setGenres(genreStorage.findAllById(film.getId()));
            film.setDirectors(directorStorage.findAllById(film.getId()));
        });
        return searchResult;
    }

    private void checkExistsFilm(Long filmId) {
        if (!filmStorage.existsById(filmId)) {
            log.warn("Фильм с id {} не найден", filmId);
            throw new ObjectNotFoundException("Фильм не найден");
        }
    }

    private void checkExistsUser(Long userId) {
        if (!userStorage.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }
}
