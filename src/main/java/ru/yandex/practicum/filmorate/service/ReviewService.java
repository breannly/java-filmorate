package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.EventType;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.model.entity.OperationType;
import ru.yandex.practicum.filmorate.model.entity.Review;
import ru.yandex.practicum.filmorate.storage.film.dao.ReviewStorageDao;
import ru.yandex.practicum.filmorate.storage.user.dao.EventStorageDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final EventStorageDao eventStorage;
    private final ReviewStorageDao reviewStorage;
    private final ValidationService validationService;

    public List<Review> findAll(Long filmId, int count) {
        if (filmId != null) validationService.checkExistsFilm(filmId);

        log.info("Получение отзывов по фильму {}", filmId);
        return reviewStorage.findAll(filmId, count);
    }

    public Review findById(Long reviewId) {
        Review foundReview = reviewStorage.findById(reviewId).orElseThrow(() ->
                new ObjectNotFoundException(reviewId, Genre.class.getSimpleName()));
        log.info("Получение отзыва с id {}", reviewId);
        return foundReview;
    }

    public Review add(Review review) {
        validationService.checkExistsUser(review.getUserId());
        validationService.checkExistsFilm(review.getFilmId());

        Review addedReview = reviewStorage.add(review);
        eventStorage.add(review.getUserId(), EventType.REVIEW, OperationType.ADD, review.getReviewId());
        log.info("Добавление нового отзыва с id {}", addedReview.getReviewId());
        return addedReview;
    }

    public Review update(Review review) {
        validationService.checkExistsReview(review.getReviewId());

        log.info("Обновление отзыва с id {}", review.getReviewId());
        Long reviewAuthorId = findById(review.getReviewId()).getUserId();
        eventStorage.add(reviewAuthorId, EventType.REVIEW, OperationType.UPDATE, review.getReviewId());
        return reviewStorage.update(review);
    }

    public void delete(Long reviewId) {
        validationService.checkExistsReview(reviewId);

        log.info("Удаление отзыва с id {}", reviewId);
        Review review = findById(reviewId);
        eventStorage.add(review.getUserId(), EventType.REVIEW, OperationType.REMOVE, review.getReviewId());
        reviewStorage.delete(reviewId);
    }

    public void addLike(Long reviewId, Long userId) {
        validationService.checkExistsReview(reviewId);
        validationService.checkExistsUser(userId);

        log.info("Пользователь {} добавил лайк отзыву {}", reviewId, userId);
        reviewStorage.addLike(reviewId, userId);
    }

    public void addDislike(Long reviewId, Long userId) {
        validationService.checkExistsReview(reviewId);
        validationService.checkExistsUser(userId);

        log.info("Пользователь {} добавил дизлайк отзыву {}", reviewId, userId);
        reviewStorage.addDislike(reviewId, userId);
    }

    public void deleteLike(Long reviewId, Long userId) {
        validationService.checkExistsReview(reviewId);
        validationService.checkExistsUser(userId);

        log.info("Пользователь {} удалил лайк у отзыва {}", reviewId, userId);
        reviewStorage.deleteLike(reviewId, userId);
    }

    public void deleteDislike(Long reviewId, Long userId) {
        validationService.checkExistsReview(reviewId);
        validationService.checkExistsUser(userId);

        log.info("Пользователь {} удалил дизлайк у отзыва {}", reviewId, userId);
        reviewStorage.deleteDislike(reviewId, userId);
    }

}
