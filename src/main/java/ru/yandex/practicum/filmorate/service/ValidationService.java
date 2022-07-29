package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.film.dao.*;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorageDao;


import static ru.yandex.practicum.filmorate.config.Config.validateDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {
    private final MpaStorageDao mpaStorage;
    private final FilmStorageDao filmStorage;
    private final UserStorageDao userStorage;
    private final GenreStorageDao genreStorage;
    private final ReviewStorageDao reviewStorage;
    private final DirectorStorageDao directorStorage;

    protected void validate(Film film) {
        boolean isWrongReleaseDate = film.getReleaseDate().isBefore(validateDate);

        if (isWrongReleaseDate) {
            log.warn("Слишком ранняя дата релиза");
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }

    protected void validate(User user) {
        boolean isWrongName = user.getName().isBlank();

        if (isWrongName) {
            log.warn("У пользователя {} user изменено имя на {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }


    protected void checkExistsFilm(Long filmId) {
        if (!filmStorage.existsById(filmId)) {
            log.warn("Фильм с id {} не найден", filmId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
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

    protected void checkExistsMpa(Long mpaId) {
        if (!mpaStorage.existsById(mpaId)) {
            log.info("Mpa с id {} не найден", mpaId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }

    protected void checkExistsGenre(Long genreId) {
        if (!genreStorage.existsById(genreId)) {
            log.warn("Жанр с id {} не найден", genreId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }
}
