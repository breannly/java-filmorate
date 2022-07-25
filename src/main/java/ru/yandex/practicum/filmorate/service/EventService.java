package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.EventType;
import ru.yandex.practicum.filmorate.model.entity.OperationType;
import ru.yandex.practicum.filmorate.storage.user.dao.EventStorageDao;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventStorageDao eventStorage;

    public void add(Long userId, EventType eventType, OperationType operationType, Long entityId) {
        log.info("Обновление истории действий: пользователь {}, операция {}, объект {}", userId, eventType, entityId);
        eventStorage.add(userId, eventType, operationType, entityId);
    }
}
