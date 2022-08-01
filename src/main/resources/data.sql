MERGE INTO GENRES (GENRE_ID, NAME)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

MERGE INTO MPA (MPA_ID, NAME, DESCRIPTION)
VALUES (1, 'G', 'Фильм без возрастных ограничений'),
       (2, 'PG', 'Детям рекомендуется смотреть фильм с родителями'),
       (3, 'PG-13', 'Детям до 13 лет просмотр не желателен'),
       (4, 'R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
       (5, 'NC-17', 'Лицам до 18 лет просмотр запрещён');

MERGE INTO FRIENDSHIP_STATUSES (STATUS_ID, STATUS)
VALUES (1, 'CONFIRMED'),
       (2, 'UNCONFIRMED');

MERGE INTO REACTIONS (REACTION_ID, NAME, MARK)
VALUES (1, 'DISLIKE', -1),
       (2, 'LIKE', 1);
