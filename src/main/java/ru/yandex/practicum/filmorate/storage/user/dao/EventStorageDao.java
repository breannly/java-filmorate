package ru.yandex.practicum.filmorate.storage.user.dao;

import ru.yandex.practicum.filmorate.model.entity.EventType;
import ru.yandex.practicum.filmorate.model.entity.OperationType;

public interface EventStorageDao {

    void add(Long userId, EventType eventType, OperationType operationType, Long entityId);
}
