package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.entity.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaStorageDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorageDao mpaStorage;

    public List<Mpa> findAll() {
        log.info("Получени списка всех mpa");
        return mpaStorage.findAll();
    }

    public Mpa findMpaById(Long mpaId) {
        Mpa foundMpa = mpaStorage.findById(mpaId).orElseThrow(()
                -> new ObjectNotFoundException(mpaId, Mpa.class.getSimpleName()));

        log.info("Получение mpa c id {}", mpaId);
        return foundMpa;
    }
}
