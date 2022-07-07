CREATE TABLE IF NOT EXISTS USERS (
    id int AUTO_INCREMENT PRIMARY KEY,
    email varchar(20),
    login varchar(20),
    name varchar(20),
    birthday date
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP_STATUSES (
    status_id int AUTO_INCREMENT PRIMARY KEY,
    status varchar(20)
);

CREATE TABLE IF NOT EXISTS USER_FRIENDS (
    user_id int REFERENCES USERS (id),
    friend_id int REFERENCES USERS (id),
    status_id int DEFAULT 2,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (status_id) REFERENCES FRIENDSHIP_STATUSES (status_id)
);

CREATE TABLE IF NOT EXISTS MPA (
    mpa_id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(20),
    description varchar(80)
);

CREATE TABLE IF NOT EXISTS FILMS (
    film_id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(20),
    description varchar(200),
    release_date date,
    duration int,
    mpa_id int REFERENCES MPA (mpa_id)
);

CREATE TABLE IF NOT EXISTS GENRES  (
    genre_id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(20)
);

CREATE TABLE IF NOT EXISTS FILM_GENRES (
    film_id int REFERENCES FILMS (film_id),
    genre_id int REFERENCES GENRES (genre_id),
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS FILM_LIKES (
    film_id int REFERENCES FILMS (film_id),
    user_id int REFERENCES USERS (id)
);