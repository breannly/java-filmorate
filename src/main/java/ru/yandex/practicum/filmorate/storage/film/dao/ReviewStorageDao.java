package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorageDao {
    List<Review> findAll(Long filmId, int count);

    Optional<Review> findById(Long reviewId);

    Review add(Review review);

    Review update(Review review);

    void delete(Long reviewId);

    boolean existsById(Long reviewId);

    void addLike(Long reviewId, Long userId);

    void addDislike(Long reviewId, Long userId);

    void deleteLike(Long reviewId, Long userId);

    void deleteDislike(Long reviewId, Long userId);
}
