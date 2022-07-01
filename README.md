# <img src="https://sun9-east.userapi.com/sun9-18/s/v1/ig2/y0GFK2n4QJaMbJSz3nZ4mYY9mCa7n_-JrueipGlABufP81zNGVl9Iz3Mk5kV2Sf0apnpVhe9MjR9fzz5En0kYovi.jpg?size=200x200&quality=95&type=album" width="80" height="80">  FILMORATE 

---

Cоциальная сеть, которая поможет выбрать кино на основе того, какие фильмы вы и ваши друзья смотрите и какие оценки им ставите.

---

## Модель базы данных

![model](https://sun9-east.userapi.com/sun9-73/s/v1/ig2/gVA9mfcdUO9xEr7XAM33SxnblocuPHH0GWBKzDizswNP1KOFVy1POmmiY-p4Ktk_inwLsbW7r-vsrrh_eUJtPoDn.jpg?size=1314x640&quality=96&type=album)

#### Пример запросов:

* Получение всех фильмов

``` SQL
SELECT name
FROM filmorate_films;
```

* Получение всех пользователей

``` SQL
SELECT *
FROM filmorate_films;
```

* Получение топ N наиболее популярных фильмов

``` SQL
SELECT name
FROM filmorate_films AS ff
JOIN films_likes AS fl ON ff.film_id = fl.film_id
GROUP BY ff.name
ORDER BY COUNT(user_id) DESC
LIMIT {N}; -- N - количество фильмов
```

* Получение списка общих друзей с другим пользователем

``` SQL
SELECT *
FROM filmorate_users
WHERE EXISTS
    (SELECT *
     FROM users_friends
     WHERE users_friends.from_id = {first_id}
       AND users_friends.status_id = 1
       AND users_friends.to_id = filmorate_users.id )
  AND EXISTS
    (SELECT *
     FROM users_friends
     WHERE users_friends.from_id = {second_id}
       AND users_friends.status_id = 1
       AND users_friends.to_id = filmorate_users.id );
```
---
![GitHub forks](https://img.shields.io/github/forks/Arnulogus/java-filmorate?style=for-the-badge)
[![TELEGRAM](https://img.shields.io/badge/Telegram-2CA5E0?style=for-the-badge&logo=telegram&logoColor=white)](https://t.me/+fZDDsVpqVSk3ZDgy)
