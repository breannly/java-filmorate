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

    public List<Director> findAll() {
        log.info("Получение списка всех жанров");
        return directorStorage.findAll();
    }

    public Director findById(Long directorId) {
        checkExistsDirector(directorId);

        log.info("Получение жанра с id {}", directorId);
        return directorStorage.findById(directorId);
    }

    public Director add(Director director) {
        Director addedDirector = directorStorage.add(director);
        log.info("Добавление нового режиссетра с id {}", addedDirector.getId());
        return addedDirector;
    }

    public Director update(Director director) {
        checkExistsDirector(director.getId());

        log.info("Обновление режиссера с id {}", director.getId());
        return directorStorage.update(director);
    }

    public void deleteDirector(Long directorId) {
        checkExistsDirector(directorId);

        log.info("Удаление режиссера с id {}", directorId);
        directorStorage.deleteDirector(directorId);
    }

    private void checkExistsDirector(Long directorId) {
        if (!directorStorage.existsById(directorId)) {
            log.warn("Режиссер с id {} не найден", directorId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
    }
}
