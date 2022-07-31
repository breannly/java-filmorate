package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.Director;
import ru.yandex.practicum.filmorate.storage.film.dao.DirectorStorageDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorageDao directorStorage;
    private final ValidationService validationService;

    public List<Director> findAll() {
        log.info("Получение списка всех режиссеров");
        return directorStorage.findAll();
    }

    public Director findById(Long directorId) {
        Director foundDirector = directorStorage.findById(directorId).orElseThrow(()
                -> new ObjectNotFoundException(directorId, Director.class.getSimpleName()));

        log.info("Получение режиссера с id {}", directorId);
        return foundDirector;
    }

    public Director add(Director director) {
        Director addedDirector = directorStorage.add(director);
        log.info("Добавление нового режиссера с id {}", addedDirector.getId());
        return addedDirector;
    }

    public Director update(Director director) {
        validationService.checkExistsDirector(director.getId());

        log.info("Обновление режиссера с id {}", director.getId());
        return directorStorage.update(director);
    }

    public void deleteDirector(Long directorId) {
        if (directorStorage.deleteDirector(directorId) == 0) {
            throw new ObjectNotFoundException(directorId, Director.class.getSimpleName());
        }
        log.info("Удаление режиссера с id {}", directorId);
    }
}
