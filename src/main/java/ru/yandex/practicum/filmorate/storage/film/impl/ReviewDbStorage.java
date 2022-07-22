package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.entity.Review;
import ru.yandex.practicum.filmorate.storage.film.dao.ReviewStorageDao;
import ru.yandex.practicum.filmorate.storage.mapper.ReviewMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final ReviewMapper reviewMapper;

    private static final String SQL_QUERY_FIND_ALL_REVIEWS = "SELECT * FROM REVIEWS LIMIT ?";
    private static final String SQL_QUERY_FIND_ALL_REVIEWS_BY_FILM = "SELECT * FROM REVIEWS " +
            "WHERE FILM_ID = ? LIMIT ?";
    private static final String SQL_QUERY_FIND_REVIEW_BY_ID = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ?";
    private static final String SQL_QUERY_ADD_REVIEW = "INSERT INTO REVIEWS " +
            "(content, is_positive, user_id, film_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_QUERY_UPDATE_REVIEW = "UPDATE REVIEWS " +
            "SET content = ?, is_positive = ?, user_id = ?, film_id = ? WHERE REVIEW_ID = ?";
    private static final String SQL_QUERY_DELETE_REVIEW = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
    private static final String SQL_QUERY_CHECK_REVIEW = "SELECT COUNT(*) FROM REVIEWS WHERE REVIEW_ID = ?";

    @Override
    public List<Review> findAll(Long filmId, int count) {
        if (filmId == 0) {
            return jdbcTemplate.query(SQL_QUERY_FIND_ALL_REVIEWS, reviewMapper, count);
        }
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_REVIEWS_BY_FILM, reviewMapper, filmId, count);
    }

    @Override
    public Review findById(Long reviewId) {
        return jdbcTemplate.queryForObject(SQL_QUERY_FIND_REVIEW_BY_ID, reviewMapper, reviewId);
    }

    @Override
    public Review add(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_QUERY_ADD_REVIEW, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            return stmt;
        }, keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return review;
    }

    @Override
    public Review update(Review review) {
        jdbcTemplate.update(SQL_QUERY_UPDATE_REVIEW,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getReviewId());

        return review;
    }

    @Override
    public void delete(Long reviewId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_REVIEW, reviewId);
    }

    @Override
    public boolean existsById(Long reviewId) {
        Integer count = jdbcTemplate.queryForObject(SQL_QUERY_CHECK_REVIEW, Integer.class, reviewId);
        assert count != null;
        return count.equals(1);
    }
}
