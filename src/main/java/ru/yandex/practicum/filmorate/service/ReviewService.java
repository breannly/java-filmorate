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
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        List<Review> reviews = reviewStorage.findAll(filmId, count);
//        reviews.forEach(review -> review.setUseful(reviewStorage.calculateUseful(review.getReviewId())));
        return reviews;
    }

    public Review findById(Long reviewId) {
        if (!reviewStorage.existsById(reviewId)) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        Review review = reviewStorage.findById(reviewId);
//        review.setUseful(reviewStorage.calculateUseful(reviewId));
        return review;
    }

    public Review add(Review review) {
        if (!userStorage.existsById(review.getUserId()) || !filmStorage.existsById(review.getFilmId())) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        return reviewStorage.add(review);
    }

    public Review update(Review review) {
        if (!reviewStorage.existsById(review.getReviewId())) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        return reviewStorage.update(review);
    }

    public void delete(Long reviewId) {
        if (!reviewStorage.existsById(reviewId)) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        reviewStorage.delete(reviewId);
    }

    public void addLike(Long reviewId, Long userId) {
        if (!reviewStorage.existsById(reviewId) || !userStorage.existsById(userId)) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        reviewStorage.addLike(reviewId, userId);
    }

    public void addDislike(Long reviewId, Long userId) {
        if (!reviewStorage.existsById(reviewId) || !userStorage.existsById(userId)) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        reviewStorage.addDislike(reviewId, userId);
    }

    public void deleteLike(Long reviewId, Long userId) {
        if (!reviewStorage.existsById(reviewId) || !userStorage.existsById(userId)) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        reviewStorage.deleteLike(reviewId, userId);
    }

    public void deleteDislike(Long reviewId, Long userId) {
        if (!reviewStorage.existsById(reviewId) || !userStorage.existsById(userId)) {
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        reviewStorage.deleteDislike(reviewId, userId);
    }
}
