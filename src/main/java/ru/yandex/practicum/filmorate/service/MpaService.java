package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaStorageDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorageDao mpaStorage;

    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    public Mpa findGenreById(Long genreId) {
        return mpaStorage.findMpaById(genreId);
    }
}
