CREATE TABLE IF NOT EXISTS USERS
(
    user_id  int AUTO_INCREMENT PRIMARY KEY,
    email    varchar(20),
    login    varchar(20),
    name     varchar(20),
    birthday date
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP_STATUSES
(
    status_id int AUTO_INCREMENT PRIMARY KEY,
    status    varchar(20)
);

CREATE TABLE IF NOT EXISTS USER_FRIENDS
(
    user_id   int REFERENCES USERS (user_id) ON DELETE CASCADE,
    friend_id int REFERENCES USERS (user_id) ON DELETE CASCADE,
    status_id int DEFAULT 2,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (status_id) REFERENCES FRIENDSHIP_STATUSES (status_id)
);

CREATE TABLE IF NOT EXISTS MPA
(
    mpa_id      int AUTO_INCREMENT PRIMARY KEY,
    name        varchar(20),
    description varchar(80)
);

CREATE TABLE IF NOT EXISTS FILMS
(
    film_id      int AUTO_INCREMENT PRIMARY KEY,
    name         varchar(255),
    description  varchar(255),
    release_date date,
    duration     int,
    rate         float,
    mpa_id       int REFERENCES MPA (mpa_id)
);

CREATE TABLE IF NOT EXISTS GENRES
(
    genre_id int AUTO_INCREMENT PRIMARY KEY,
    name     varchar(20)
);

CREATE TABLE IF NOT EXISTS FILM_GENRES
(
    film_id  int REFERENCES FILMS (film_id) ON DELETE CASCADE,
    genre_id int REFERENCES GENRES (genre_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS FILM_MARKS
(
    film_id   int REFERENCES FILMS (film_id) ON DELETE CASCADE,
    user_id   int REFERENCES USERS (user_id) ON DELETE CASCADE,
    film_mark int,
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS REVIEWS
(
    review_id   int AUTO_INCREMENT PRIMARY KEY,
    content     varchar(200),
    is_positive bool,
    user_id     int REFERENCES USERS (user_id) ON DELETE CASCADE,
    film_id     int REFERENCES FILMS (film_id) ON DELETE CASCADE,
    useful      int DEFAULT 0
);

CREATE TABLE IF NOT EXISTS REACTIONS
(
    reaction_id int AUTO_INCREMENT PRIMARY KEY,
    name        varchar(20),
    mark        int
);

CREATE TABLE IF NOT EXISTS REVIEW_REACTIONS
(
    review_id   int REFERENCES REVIEWS (review_id) ON DELETE CASCADE,
    user_id     int REFERENCES USERS (user_id) ON DELETE CASCADE,
    reaction_id int REFERENCES REACTIONS (reaction_id),
    PRIMARY KEY (review_id, user_id)
);

CREATE TABLE IF NOT EXISTS DIRECTORS
(
    director_id int AUTO_INCREMENT PRIMARY KEY,
    name        varchar(255)
);

CREATE TABLE IF NOT EXISTS FILM_DIRECTORS
(
    film_id     int REFERENCES FILMS (film_id) ON DELETE CASCADE,
    director_id int REFERENCES DIRECTORS (director_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, director_id)
);

CREATE TABLE IF NOT EXISTS EVENTS
(
    event_id       int AUTO_INCREMENT PRIMARY KEY,
    user_id        int REFERENCES USERS (user_id) ON DELETE CASCADE,
    event_time     time,
    event_type     varchar(6),
    operation_type varchar(6),
    entity_id      int
);