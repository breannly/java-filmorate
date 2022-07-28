package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.film.dao.DirectorStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.ReviewStorageDao;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorageDao;


import static ru.yandex.practicum.filmorate.config.Config.validateDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {

    private FilmStorageDao filmStorage;
    private UserStorageDao userStorage;
    private DirectorStorageDao directorStorage;
    private ReviewStorageDao reviewStorage;

    protected void validateFilm(Film film) {
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);

        if (isWrongReleaseDate) {
            log.warn("Слишком ранняя дата релиза");
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }

    protected void validateUser(User user) {
        boolean isWrongName = user.getName().isBlank();

        if (isWrongName) {
            log.warn("У пользователя {} user изменено имя на {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }

    protected void checkExistsFilm(Long filmId) {
        if (!filmStorage.existsById(filmId)) {
            log.warn("Фильм с id {} не найден", filmId);
            throw new ObjectNotFoundException("Фильм не найден");
        }
    }

    protected void checkExistsUser(Long userId) {
        if (!userStorage.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }

    protected void checkExistsDirector(Long directorId) {
        if (!directorStorage.existsById(directorId)) {
            log.warn("Режиссер с id {} не найден", directorId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }

    protected void checkExistsReview(Long reviewId) {
        if (!reviewStorage.existsById(reviewId)) {
            log.warn("Пользователь с id {} не найден", reviewId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }
}
