package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.entity.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorageDao {
    List<Mpa> findAll();

    Optional<Mpa> findById(Long mpaId);

    boolean existsById(Long mpaId);
}
