# java-filmorate
## Модель базы данных (ER-диаграмма)

![filmorate.png](filmorate.png)

### Примеры запросов в базу данных

#### Пользователи

Получение списка всех пользователей

```
SELECT * 
FROM users
```

Получить пользователя по id
```
SELECT * 
FROM users
WHERE user_id = 1
```

Получить список друзей пользователя

```
SELECT u.name AS friend_name,
       u.login AS friend_login,
       sf.name AS status_friendship
FROM friendship f
LEFT JOIN users u ON f.user_friend = u.user_id
LEFT JOIN status_friendship sf ON f.status_friendship = sf.status_id
WHERE f.user_id=1

```

#### Фильмы

Получение списка всех фильмов

```
SELECT * 
FROM films
```

Получить фильм по id
```
SELECT * 
FROM films
WHERE film_id = 1
```

Узнать жанры фильма

```
SELECT name AS genre
FROM films f
LEFT JOIN film_genre fg ON f.film_id =fg.film_id
LEFT JOIN genre g ON fg.genre_id = g.genre_id
WHERE f.film_id = 1
```

Получить топ 10 фильмов
```
SELECT f.name AS film,
       count(fl.film_id) AS likes
FROM film_likes AS fl
LEFT JOIN films f ON fl.film_id=f.film_id
GROUP BY fl.film_id
ORDER BY count(fl.film_id) DESC
LIMIT 10
```


