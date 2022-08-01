package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.EventType;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.OperationType;
import ru.yandex.practicum.filmorate.storage.film.dao.DirectorStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.MarkStorageDao;
import ru.yandex.practicum.filmorate.storage.user.dao.EventStorageDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorageDao filmStorage;
    private final GenreStorageDao genreStorage;
    private final EventStorageDao eventStorage;
    private final DirectorStorageDao directorStorage;
    private final MarkStorageDao markStorage;
    private final ValidationService validationService;

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
        validationService.validate(film);

        filmStorage.add(film);
        film.setGenres(genreStorage.addToFilm(film.getId(), film.getGenres()));
        film.setDirectors(directorStorage.addToFilm(film.getId(), film.getDirectors()));
        log.info("Добавление нового фильма c id {}", film.getId());

        return film;
    }

    public Film update(Film film) {
        validationService.validate(film);
        validationService.checkExistsFilm(film.getId());

        filmStorage.update(film);
        genreStorage.addToFilm(film.getId(), film.getGenres());
        if (film.getGenres() != null) film.setGenres(genreStorage.findAllById(film.getId()));
        directorStorage.addToFilm(film.getId(), film.getDirectors());
        if (film.getDirectors() != null) film.setDirectors(directorStorage.findAllById(film.getId()));
        log.info("Обновление фильма с id {}", film.getId());

        return film;
    }

    public void deleteFilm(Long filmId) {
        log.info("Удаление фильма с id {}", filmId);
        filmStorage.deleteFilm(filmId);
    }

    public Film findFilmById(Long filmId) {
        Film foundFilm = filmStorage.findById(filmId).orElseThrow(() ->
                new ObjectNotFoundException(filmId, Film.class.getSimpleName()));
        foundFilm.setGenres(genreStorage.findAllById(filmId));
        foundFilm.setDirectors(directorStorage.findAllById(filmId));
        log.info("Получение фильма с id {}", filmId);
        return foundFilm;
    }

    public List<Film> findPopularFilms(int count, Long genreId, int year) {
        if (genreId != null) validationService.checkExistsGenre(genreId);
        List<Film> popularFilms = filmStorage.findPopularFilms(count, genreId, year);
        popularFilms.forEach(film -> {
            film.setGenres(genreStorage.findAllById(film.getId()));
            film.setDirectors(directorStorage.findAllById(film.getId()));
        });
        log.info("Получение {} фильмов", count);
        return popularFilms;
    }

    public void addMark(Long filmId, Long userId, int mark) {
        validationService.checkExistsFilm(filmId);
        validationService.checkExistsUser(userId);

        log.info("Пользователь {} поставил фильму {} оценку {}", userId, filmId, mark);
        markStorage.addMark(filmId, userId, mark);
        eventStorage.add(userId, EventType.LIKE, OperationType.ADD, filmId);
    }

     public void deleteMark(Long filmId, Long userId) {
        validationService.checkExistsFilm(filmId);
        validationService.checkExistsUser(userId);

        log.info("Пользователь {} удалил оценку у фильма {}", userId, filmId);
        markStorage.deleteMark(filmId, userId);
        eventStorage.add(userId, EventType.LIKE, OperationType.REMOVE, filmId);
    }
    public List<Film> findCommonFilms(Long userId, Long friendId) {
        validationService.checkExistsUser(userId);
        validationService.checkExistsUser(friendId);

        log.info("Получение общих фильмов для пользователя {} и {}", userId, friendId);
        return filmStorage.findCommonFilmsForUsers(userId, friendId);
    }

    public List<Film> findFilmsByDirector(Long directorId, String sortBy) {
        validationService.checkExistsDirector(directorId);
        log.info("Получение фильмов режиссера {} отсортированных по {}", directorId, sortBy);
        List<Film> films = filmStorage.findFilmsByDirector(directorId, sortBy);
        films.forEach(film -> {
            film.setGenres(genreStorage.findAllById(film.getId()));
            film.setDirectors(directorStorage.findAllById(film.getId()));
        });
        return films;
    }

    public List<Film> getRecommendations(Long userId) {
        validationService.checkExistsUser(userId);

        List<Film> recommendationsFilms = filmStorage.getRecommendations(userId);
        recommendationsFilms.forEach(film -> {
            film.setGenres(genreStorage.findAllById(film.getId()));
            film.setDirectors(directorStorage.findAllById(film.getId()));
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
}
