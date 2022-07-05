CREATE TABLE IF NOT EXISTS filmorate_users (
    id int AUTO_INCREMENT PRIMARY KEY,
    email varchar(20),
    login varchar(20),
    name varchar(20),
    birthday date
);

CREATE TABLE IF NOT EXISTS friendship_statuses (
    status_id int AUTO_INCREMENT PRIMARY KEY,
    status varchar(20)
);

CREATE TABLE IF NOT EXISTS user_friends (
    user_id int REFERENCES filmorate_users (id),
    friend_id int REFERENCES filmorate_users (id),
    status_id int DEFAULT 2,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (status_id) REFERENCES friendship_statuses (status_id)
);

CREATE TABLE IF NOT EXISTS rates (
    rate_id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(20),
    description varchar(80)
);

CREATE TABLE IF NOT EXISTS filmorate_films (
    film_id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(20),
    description varchar(20),
    release_date date,
    duration int,
    rate_id int REFERENCES rates (rate_id)
);

CREATE TABLE IF NOT EXISTS genres  (
    genre_id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(20),
    description varchar(20)
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id int REFERENCES filmorate_films (film_id),
    genre_id int REFERENCES genres (genre_id)
);

CREATE TABLE IF NOT EXISTS films_likes (
    film_id int REFERENCES filmorate_films (film_id),
    user_id int REFERENCES filmorate_users (id)
);