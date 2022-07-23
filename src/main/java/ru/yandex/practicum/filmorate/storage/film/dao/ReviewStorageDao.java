package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.entity.Review;

import java.util.List;

public interface ReviewStorageDao {
    List<Review> findAll(Long filmId, int count);

    Review findById(Long reviewId);

    Review add(Review review);

    Review update(Review review);

    boolean existsById(Long reviewId);

    void delete(Long reviewId);

    void addLike(Long reviewId, Long userId);

    void addDislike(Long reviewId, Long userId);

    void deleteLike(Long reviewId, Long userId);

    void deleteDislike(Long reviewId, Long userId);
}
