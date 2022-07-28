package ru.yandex.practicum.filmorate.storage.film.dao;

public interface MarkStorageDao {
    void addMark(Long id, Long userId, int mark);

}
