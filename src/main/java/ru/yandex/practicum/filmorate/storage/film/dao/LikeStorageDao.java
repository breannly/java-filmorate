package ru.yandex.practicum.filmorate.storage.film.dao;

public interface LikeStorageDao {
    public void addLike(Long id, Long filmId);

    public void deleteLike(Long id, Long filmId);
}
