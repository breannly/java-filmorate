package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.Review;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.ReviewStorageDao;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorageDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorageDao reviewStorage;
    private final FilmStorageDao filmStorage;
    private final UserStorageDao userStorage;

    public List<Review> findAll(Long filmId, int count) {
        if (filmId != 0 && (!filmStorage.existsById(filmId))) {
            log.warn("Фильм с id {} не найден", filmId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Получение отзывов по фильму {}", filmId);
        return reviewStorage.findAll(filmId, count);
    }

    public Review findById(Long reviewId) {
        if (!reviewStorage.existsById(reviewId)) {
            log.warn("Отзыв с id {} не найден", reviewId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Получение отзыва с id {}", reviewId);
        return reviewStorage.findById(reviewId);
    }

    public Review add(Review review) {
        if (!(userStorage.existsById(review.getUserId()) && filmStorage.existsById(review.getFilmId()))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        Review addedReview = reviewStorage.add(review);
        log.info("Добавление нового отзыва с id {}", addedReview.getReviewId());
        return addedReview;
    }

    public Review update(Review review) {
        if (!(userStorage.existsById(review.getUserId()) && filmStorage.existsById(review.getFilmId()))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Обновление отзыва с id {}", review.getReviewId());
        return reviewStorage.update(review);
    }

    public void delete(Long reviewId) {
        if (!reviewStorage.existsById(reviewId)) {
            log.warn("Отзыв с id {} не найден", reviewId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Удаление отзыва с id {}", reviewId);
        reviewStorage.delete(reviewId);
    }

    public void addLike(Long reviewId, Long userId) {
        if (!(reviewStorage.existsById(reviewId) && userStorage.existsById(userId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Пользователь {} добавил лайк отзыву {}", reviewId, userId);
        reviewStorage.addLike(reviewId, userId);
    }

    public void addDislike(Long reviewId, Long userId) {
        if (!(reviewStorage.existsById(reviewId) && userStorage.existsById(userId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Пользователь {} добавил дизлайк отзыву {}", reviewId, userId);
        reviewStorage.addDislike(reviewId, userId);
    }

    public void deleteLike(Long reviewId, Long userId) {
        if (!(reviewStorage.existsById(reviewId) && userStorage.existsById(userId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Пользователь {} удалил лайк у отзыва {}", reviewId, userId);
        reviewStorage.deleteLike(reviewId, userId);
    }

    public void deleteDislike(Long reviewId, Long userId) {
        if (!(reviewStorage.existsById(reviewId) && userStorage.existsById(userId))) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Пользователь {} удалил дизлайк у отзыва {}", reviewId, userId);
        reviewStorage.deleteDislike(reviewId, userId);
    }
}
