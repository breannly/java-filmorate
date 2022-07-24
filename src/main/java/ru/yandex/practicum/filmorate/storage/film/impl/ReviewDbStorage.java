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

    private static final String SQL_QUERY_FIND_ALL_REVIEWS = "SELECT R.*, NVL(SUM(MARK), 0) AS MARK " +
            "FROM REVIEWS AS R " +
            "LEFT JOIN REVIEW_REACTIONS AS RR ON R.REVIEW_ID = RR.REVIEW_ID " +
            "LEFT JOIN REACTIONS AS RE ON RR.REVIEW_ID = RE.REACTION_ID " +
            "GROUP BY R.REVIEW_ID ORDER BY MARK DESC LIMIT ?";
    private static final String SQL_QUERY_FIND_ALL_REVIEWS_BY_FILM = "SELECT R.*, NVL(SUM(MARK), 0) AS MARK " +
            "FROM REVIEWS AS R " +
            "LEFT JOIN REVIEW_REACTIONS AS RR ON R.REVIEW_ID = RR.REVIEW_ID " +
            "LEFT JOIN REACTIONS AS RE ON RR.REVIEW_ID = RE.REACTION_ID " +
            "WHERE FILM_ID = ? GROUP BY R.REVIEW_ID ORDER BY MARK DESC LIMIT ?";
    private static final String SQL_QUERY_FIND_REVIEW_BY_ID = "SELECT R.*, NVL(SUM(MARK), 0) AS MARK " +
            "FROM REVIEWS AS R " +
            "LEFT JOIN REVIEW_REACTIONS AS RR ON R.REVIEW_ID = RR.REVIEW_ID " +
            "LEFT JOIN REACTIONS AS RE ON RR.REVIEW_ID = RE.REACTION_ID " +
            "WHERE R.REVIEW_ID = ? GROUP BY R.REVIEW_ID";
    private static final String SQL_QUERY_ADD_REVIEW = "INSERT INTO REVIEWS " +
            "(content, is_positive, user_id, film_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_QUERY_UPDATE_REVIEW = "UPDATE REVIEWS " +
            "SET content = ?, is_positive = ? WHERE REVIEW_ID = ?";
    private static final String SQL_QUERY_DELETE_REVIEW = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
    private static final String SQL_QUERY_CHECK_REVIEW = "SELECT COUNT(*) FROM REVIEWS WHERE REVIEW_ID = ?";
    private static final String SQL_QUERY_ADD_LIKE = "MERGE INTO REVIEW_REACTIONS " +
            "VALUES (?, ?, 2)";
    private static final String SQL_QUERY_ADD_DISLIKE = "MERGE INTO REVIEW_REACTIONS " +
            "VALUES (?, ?, 1)";
    private static final String SQL_QUERY_DELETE_LIKE = "DELETE FROM REVIEW_REACTIONS " +
            "WHERE REVIEW_ID = ? AND USER_ID = ?";
    private static final String SQL_QUERY_DELETE_DISLIKE = "DELETE FROM REVIEW_REACTIONS " +
            "WHERE REVIEW_ID = ? AND USER_ID = ?";
    private static final String SQL_QUERY_CALCULATE_USEFUL = "SELECT SUM(MARK) FROM REVIEW_REACTIONS AS RR " +
            "JOIN REACTIONS AS RE ON RR.REACTION_ID = RE.REACTION_ID " +
            "WHERE REVIEW_ID = ? GROUP BY REVIEW_ID";

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

    @Override
    public void addLike(Long reviewId, Long userId) {
        jdbcTemplate.update(SQL_QUERY_ADD_LIKE, reviewId, userId);
    }

    @Override
    public void addDislike(Long reviewId, Long userId) {
        jdbcTemplate.update(SQL_QUERY_ADD_DISLIKE, reviewId, userId);
    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_LIKE, reviewId, userId);
    }

    @Override
    public void deleteDislike(Long reviewId, Long userId) {
        jdbcTemplate.update(SQL_QUERY_DELETE_DISLIKE, reviewId, userId);
    }
}
