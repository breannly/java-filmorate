package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.EventType;
import ru.yandex.practicum.filmorate.model.entity.OperationType;
import ru.yandex.practicum.filmorate.model.entity.Review;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.storage.film.dao.ReviewStorageDao;
import ru.yandex.practicum.filmorate.storage.user.dao.EventStorageDao;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorageDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorageDao reviewStorage;
    private final FilmStorageDao filmStorage;
    private final UserStorageDao userStorage;
    private final EventStorageDao eventStorage;

    public List<Review> findAll(Long filmId, int count) {
        if (filmId != 0 ) checkExistsFilm(filmId);

        log.info("Получение отзывов по фильму {}", filmId);
        return reviewStorage.findAll(filmId, count);
    }

    public Review findById(Long reviewId) {
        checkExistsReview(reviewId);

        log.info("Получение отзыва с id {}", reviewId);
        return reviewStorage.findById(reviewId);
    }

    public Review add(Review review) {
        checkExistsUser(review.getUserId());
        checkExistsFilm(review.getFilmId());

        Review addedReview = reviewStorage.add(review);
        eventStorage.add(review.getUserId(), EventType.REVIEW, OperationType.ADD, review.getReviewId());
        log.info("Добавление нового отзыва с id {}", addedReview.getReviewId());
        return addedReview;
    }

    public Review update(Review review) {
        checkExistsReview(review.getReviewId());

        log.info("Обновление отзыва с id {}", review.getReviewId());
        Long reviewAuthorId = findById(review.getReviewId()).getUserId();
        eventStorage.add(reviewAuthorId, EventType.REVIEW, OperationType.UPDATE, review.getReviewId());
        return reviewStorage.update(review);
    }

    public void delete(Long reviewId) {
        checkExistsReview(reviewId);

        log.info("Удаление отзыва с id {}", reviewId);
        Review review = findById(reviewId);
        eventStorage.add(review.getUserId(), EventType.REVIEW, OperationType.REMOVE, review.getReviewId());
        reviewStorage.delete(reviewId);
    }

    public void addLike(Long reviewId, Long userId) {
        checkExistsReview(reviewId);
        checkExistsUser(userId);

        log.info("Пользователь {} добавил лайк отзыву {}", reviewId, userId);
        reviewStorage.addLike(reviewId, userId);
    }

    public void addDislike(Long reviewId, Long userId) {
        checkExistsReview(reviewId);
        checkExistsUser(userId);

        log.info("Пользователь {} добавил дизлайк отзыву {}", reviewId, userId);
        reviewStorage.addDislike(reviewId, userId);
    }

    public void deleteLike(Long reviewId, Long userId) {
        checkExistsReview(reviewId);
        checkExistsUser(userId);

        log.info("Пользователь {} удалил лайк у отзыва {}", reviewId, userId);
        reviewStorage.deleteLike(reviewId, userId);
    }

    public void deleteDislike(Long reviewId, Long userId) {
        checkExistsReview(reviewId);
        checkExistsUser(userId);

        log.info("Пользователь {} удалил дизлайк у отзыва {}", reviewId, userId);
        reviewStorage.deleteDislike(reviewId, userId);
    }

    private void checkExistsReview(Long reviewId) {
        if (!reviewStorage.existsById(reviewId)) {
            log.warn("Пользователь с id {} не найден", reviewId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }

    private void checkExistsUser(Long userId) {
        if (!userStorage.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }

    private void checkExistsFilm(Long filmId) {
        if (!filmStorage.existsById(filmId)) {
            log.warn("Фильм с id {} не найден", filmId);
            throw new ObjectNotFoundException("Фильм не найден");
        }
    }
}
