INSERT INTO GENRES (name, description)
VALUES ('COMEDY', 'Комедия'),
       ('DRAMA', 'Драма'),
       ('CARTOON', 'Мультфильм'),
       ('THRILLER', 'Триллер'),
       ('DOCUMENTARY', 'Документальный'),
       ('ACTION', 'Боевик');

INSERT INTO RATES (name, description)
VALUES ('G', 'Фильм без возрастных ограничений'),
       ('PG', 'Детям рекомендуется смотреть фильм с родителями'),
       ('PG-13', 'Детям до 13 лет просмотр не желателен'),
       ('R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
       ('NC-17', 'Лицам до 18 лет просмотр запрещён');

INSERT INTO FRIENDSHIP_STATUSES (STATUS)
VALUES ('CONFIRMED'),
       ('UNCONFIRMED');