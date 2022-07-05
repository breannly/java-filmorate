package ru.yandex.practicum.filmorate.config;

import java.time.LocalDate;
import java.time.Month;

public class Config {
    // configuration for validation
    public static final LocalDate validateDate = LocalDate.of(1895, Month.DECEMBER, 28);
    public static final int MAX_SIZE_DESCRIPTION = 200;

    // configuration for user-sql-query
    public static final String SQL_QUERY_FIND_ALL_USERS = "SELECT * FROM filmorate_users";
    public static final String SQL_QUERY_FIND_USER = "SELECT * FROM filmorate_users WHERE id = ?";
    public static final String SQL_QUERY_ADD_USER = "INSERT INTO filmorate_users(email, login, name, birthday) " +
            "values (?, ?, ?, ?)";
    public static final String SQL_QUERY_UPDATE_USER = "UPDATE filmorate_users " +
            "SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    // configuration for friend-sql-query
    public static final String SQL_QUERY_UPDATE_STATUS = "UPDATE user_friends SET status_id = ? " +
            "WHERE user_id = ? AND friend_id = ?";
    public static final String SQL_QUERY_ADD_FRIEND = "MERGE INTO user_friends (user_id, friend_id) VALUES (?, ?)";
    public static final String SQL_QUERY_GET_STATUSES = "SELECT status FROM user_friends AS uf " +
            "JOIN friendship_statuses AS fs ON uf.status_id = fs.status_id " +
            "WHERE user_id = ? AND friend_id = ? OR user_id = ? AND friend_id = ?";
    public static final String SQL_QUERY_DELETE_FRIEND = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";
    public static final String SQL_QUERY_FIND_FRIENDS = "SELECT * FROM filmorate_users AS fu " +
            "WHERE EXISTS (SELECT * FROM user_friends WHERE user_id = ? AND friend_id = fu.id)";
    public static final String SQL_QUERY_FIND_MUTUAL_FRIENDS = "SELECT * FROM filmorate_users " +
            "WHERE EXISTS " +
            "(SELECT FROM user_friends WHERE user_friends.user_id = ? AND user_friends.status_id = 1 " +
            "AND user_friends.friend_id = filmorate_users.id ) " +
            "AND EXISTS" +
            "(SELECT * FROM user_friends WHERE user_friends.user_id = ? AND user_friends.status_id = 1 " +
            "AND user_friends.friend_id = filmorate_users.id )";

    // configuration for film-sql-query
    public static final String SQL_QUERY_FIND_ALL_FILMS = "SELECT * FROM filmorate_films";
    public static final String SQL_QUERY_FIND_FILMS = "SELECT name FROM filmorate_films AS ff " +
            "JOIN films_likes AS fl ON ff.film_id = fl.film_id GROUP BY ff.name ORDER BY COUNT(user_id) DESC LIMIT ?";
    public static final String SQL_QUERY_FIND_FILM = "SELECT * FROM filmorate_films WHERE film_id = ?";
    public static final String SQL_QUERY_ADD_FILM = "INSERT INTO filmorate_films" +
            "(name, description, release_date,duration) values (?, ?, ?, ?)";
    public static final String SQL_QUERY_UPDATE_FILM = "UPDATE filmorate_films " +
            "SET name = ?, description = ?, release_date = ?, duration = ? WHERE film_id = ?";

    // configuration for like-sql-query
    public static final String SQL_QUERY_ADD_LIKE = "INSERT INTO films_likes(film_id, user_id) VALUES (?, ?)";
    public static final String SQL_QUERY_DELETE_LIKE = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
}
