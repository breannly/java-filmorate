package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreStorageDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorageDao genreStorage;

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre findGenreById(Long genreId) {
        return genreStorage.findGenreById(genreId);
    }
}
