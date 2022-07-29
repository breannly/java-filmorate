package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreStorageDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorageDao genreStorage;
    private final ValidationService validationService;

    public List<Genre> findAll() {
        log.info("Получение списка всех жанров");
        return genreStorage.findAll();
    }

    public Genre findGenreById(Long genreId) {
        validationService.checkExistsGenre(genreId);

        log.info("Получение жанра с id {}", genreId);
        return genreStorage.findById(genreId);
    }
}
