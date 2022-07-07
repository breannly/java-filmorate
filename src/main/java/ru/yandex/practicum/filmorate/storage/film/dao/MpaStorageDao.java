package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.entity.Mpa;

import java.util.List;

public interface MpaStorageDao {
    public List<Mpa> findAll();

    public Mpa findMpaById(Long mpaId);
}
