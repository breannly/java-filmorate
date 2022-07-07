package ru.yandex.practicum.filmorate.config;

import java.time.LocalDate;
import java.time.Month;

public class Config {
    // configuration for validation
    public static final LocalDate validateDate = LocalDate.of(1895, Month.DECEMBER, 28);
    public static final int MAX_SIZE_DESCRIPTION = 200;

    // configuration for user-sql-query
    public static final String SQL_QUERY_FIND_ALL_USERS = "SELECT * FROM USERS";
    public static final String SQL_QUERY_FIND_USER_BY_ID = "SELECT * FROM USERS WHERE id = ?";
    public static final String SQL_QUERY_ADD_USER = "INSERT INTO USERS(email, login, name, birthday) " +
            "values (?, ?, ?, ?)";
    public static final String SQL_QUERY_UPDATE_USER = "UPDATE USERS " +
            "SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    // configuration for friend-sql-query
    public static final String SQL_QUERY_UPDATE_STATUS = "UPDATE USER_FRIENDS SET status_id = ? " +
            "WHERE user_id = ? AND friend_id = ?";
    public static final String SQL_QUERY_ADD_FRIEND = "MERGE INTO USER_FRIENDS (user_id, friend_id) VALUES (?, ?)";
    public static final String SQL_QUERY_GET_STATUSES = "SELECT status FROM USER_FRIENDS AS uf " +
            "JOIN FRIENDSHIP_STATUSES AS fs ON uf.status_id = fs.status_id " +
            "WHERE user_id = ? AND friend_id = ? OR user_id = ? AND friend_id = ?";
    public static final String SQL_QUERY_DELETE_FRIEND = "DELETE FROM USER_FRIENDS WHERE user_id = ? AND friend_id = ?";
    public static final String SQL_QUERY_FIND_FRIENDS = "SELECT * FROM USERS AS fu " +
            "WHERE EXISTS (SELECT * FROM USER_FRIENDS WHERE user_id = ? AND friend_id = fu.id)";
    public static final String SQL_QUERY_FIND_MUTUAL_FRIENDS = "SELECT * FROM USERS " +
            "WHERE EXISTS " +
            "(SELECT FROM user_friends WHERE USER_FRIENDS.user_id = ? " +
            "AND USER_FRIENDS.friend_id = USERS.id ) " +
            "AND EXISTS" +
            "(SELECT * FROM USER_FRIENDS WHERE user_friends.user_id = ? " +
            "AND USER_FRIENDS.friend_id = USERS.id )";
    public static final String SQL_QUERY_CHECK_USER = "SELECT COUNT(*) FROM USERS WHERE id = ?";

    // configuration for film-sql-query
    public static final String SQL_QUERY_FIND_ALL_FILMS = "SELECT * FROM FILMS AS ff " +
            "JOIN MPA AS m ON ff.mpa_id = m.mpa_id";
    public static final String SQL_QUERY_FIND_FILMS = "SELECT * FROM FILMS AS ff " +
            "JOIN MPA AS m ON ff.mpa_id = m.mpa_id " +
            "LEFT JOIN FILM_LIKES AS fl ON ff.film_id = fl.film_id GROUP BY ff.name ORDER BY COUNT(user_id) DESC LIMIT ?";
    public static final String SQL_QUERY_FIND_FILM_BY_ID = "SELECT * FROM FILMS AS ff " +
            "JOIN MPA AS m ON ff.mpa_id = m.mpa_id WHERE film_id = ?";
    public static final String SQL_QUERY_FIND_GENRES = "SELECT * FROM FILM_GENRES AS fg " +
            "JOIN GENRES as g ON fg.genre_id = g.genre_id WHERE film_id = ?";
    public static final String SQL_QUERY_FIND_MPA = "SELECT * FROM FILMS AS ff " +
            "JOIN MPA AS m ON ff.mpa_id = m.mpa_id WHERE ff.film_id = ?";
    public static final String SQL_QUERY_ADD_FILM = "INSERT INTO FILMS" +
            "(name, description, release_date, duration, mpa_id) values (?, ?, ?, ?, ?)";
    public static final String SQL_QUERY_ADD_GENRE = "MERGE INTO FILM_GENRES" +
            "(film_id, genre_id) values (?, ?)";
    public static final String SQL_QUERY_UPDATE_FILM = "UPDATE FILMS " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?";
    public static final String SQL_QUERY_DELETE_GENRES = "DELETE FROM FILM_GENRES WHERE film_id = ?";
    public static final String SQL_QUERY_CHECK_FILM = "SELECT COUNT(*) FROM FILMS WHERE film_id = ?";

    // configuration for like-sql-query
    public static final String SQL_QUERY_ADD_LIKE = "INSERT INTO FILM_LIKES (film_id, user_id) VALUES (?, ?)";
    public static final String SQL_QUERY_DELETE_LIKE = "DELETE FROM FILM_LIKES WHERE film_id = ? AND user_id = ?";

    //configuration for genre-sql-query
    public static final String SQL_QUERY_FIND_ALL_GENRES = "SELECT * FROM GENRES";
    public static final String SQL_QUERY_FIND_GENRE_BY_ID = "SELECT * FROM GENRES WHERE genre_id = ?";

    //configuration for mpa-sql-query
    public static final String SQL_QUERY_FIND_ALL_MPA = "SELECT * FROM MPA";
    public static final String SQL_QUERY_FIND_MPA_BY_ID = "SELECT * FROM MPA WHERE mpa_id = ?";
}
