package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.Director;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.film.dao.DirectorStorageDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorageDao directorsStorage;

    public List<Director> findAll() {
        log.info("Получение списка всех жанров");
        return directorsStorage.findAll();
    }

    public Director findById(Long directorId) {
        if (!directorsStorage.existsById(directorId)) {
            log.warn("Режиссер с id {} не найден", directorId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Получение жанра с id {}", directorId);
        return directorsStorage.findById(directorId);
    }

    public Director add(Director director) {
        Director addedDirector = directorsStorage.add(director);
        log.info("Добавление нового режиссетра с id {}", addedDirector.getId());
        return addedDirector;
    }

    public Director update(Director director) {
        if (!directorsStorage.existsById(director.getId())) {
            log.warn("Режиссер с id {} не найден", director.getId());
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Обновление режиссера с id {}", director.getId());
        return directorsStorage.update(director);
    }

    public void deleteDirector(Long directorId) {
        if (!directorsStorage.existsById(directorId)) {
            log.warn("Режиссер с id {} не найден", directorId);
            throw new ObjectNotFoundException("Вызов несуществующего объекта");
        }
        log.info("Удаление режиссера с id {}", directorId);
        directorsStorage.deleteDirector(directorId);
    }
}
