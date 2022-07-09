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

    public List<Genre> findAll() {
        log.info("Получение списка всех жанров");
        return genreStorage.findAll();
    }

    public Genre findGenreById(Long genreId) {
        if (!genreStorage.existsById(genreId)) {
            log.warn("Жанр с id {} не найден", genreId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Получение жанра с id {}", genreId);
        return genreStorage.findById(genreId);
    }
}
