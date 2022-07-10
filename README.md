# <img src="https://sun9-east.userapi.com/sun9-18/s/v1/ig2/y0GFK2n4QJaMbJSz3nZ4mYY9mCa7n_-JrueipGlABufP81zNGVl9Iz3Mk5kV2Sf0apnpVhe9MjR9fzz5En0kYovi.jpg?size=200x200&quality=95&type=album" width="80" height="80">  FILMORATE 

---

Cоциальная сеть, которая поможет выбрать кино на основе того, какие фильмы вы и ваши друзья смотрите и какие оценки им ставите.

---

## Модель базы данных

![model](https://sun9-east.userapi.com/sun9-59/s/v1/ig2/QFju8O9HFxDjXG1jz7Sy6D-u7EIpo1UnHfJRvDmF0aKsUbNvqou3x75GiJKvLw8kGsNzt_IotSGGZ_J58uTl2w_6.jpg?size=1337x688&quality=96&type=album)

#### Пример запросов:

* Получение всех фильмов

``` SQL
SELECT *
FROM FILMS;
```

* Получение всех пользователей

``` SQL
SELECT *
FROM USERS;
```

* Получение топ N наиболее популярных фильмов

``` SQL
SELECT *
FROM FILMS AS f
JOIN MPA AS m ON f.mpa_id = m.mpa_id
LEFT JOIN FILM_LIKES AS fl ON f.film_id = fl.film_id
GROUP BY f.name
ORDER BY COUNT(user_id) DESC
LIMIT {N}; -- N - количество фильмов
```

* Получение списка общих друзей с другим пользователем

``` SQL
SELECT *
FROM USERS 
WHERE EXISTS
    (SELECT *
     FROM USER_FRIENDS
     WHERE USER_FRIENDS.user_id = {first_id}
       AND USER_FRIENDS.friend_id = USERS.USER_ID) 
  AND EXISTS
    (SELECT *
     FROM USER_FRIENDS
     WHERE user_friends.user_id = {second_id}
       AND USER_FRIENDS.friend_id = USERS.USER_ID)
```
---
![GitHub forks](https://img.shields.io/github/forks/Arnulogus/java-filmorate?style=for-the-badge)
[![TELEGRAM](https://img.shields.io/badge/Telegram-2CA5E0?style=for-the-badge&logo=telegram&logoColor=white)](https://t.me/+fZDDsVpqVSk3ZDgy)
