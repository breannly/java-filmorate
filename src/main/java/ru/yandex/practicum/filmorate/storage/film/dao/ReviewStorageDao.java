package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.entity.Review;

import java.util.List;

public interface ReviewStorageDao {
    List<Review> findAll(Long filmId, int count);

    Review findById(Long reviewId);

    Review add(Review review);

    Review update(Review review);

    void delete(Long reviewId);

    boolean existsById(Long reviewId);
}
