package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.entity.Mpa;

import java.util.List;

public interface MpaStorageDao {
    List<Mpa> findAll();

    Mpa findById(Long mpaId);

    boolean existsById(Long mpaId);
}
