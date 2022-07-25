package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.entity.EventType;
import ru.yandex.practicum.filmorate.model.entity.OperationType;
import ru.yandex.practicum.filmorate.storage.user.dao.EventStorageDao;

import java.sql.Time;
import java.time.LocalTime;

@Repository
@RequiredArgsConstructor
public class EventDbStorage implements EventStorageDao {
    private final JdbcTemplate jdbcTemplate;

    private static final Time TIMESTAMP = java.sql.Time.valueOf(LocalTime.now());
    private static final String SQL_QUERY_ADD_EVENT = "INSERT INTO EVENTS " +
            "(user_id, event_time, event_type, operation_type, entity_id) VALUES (?, ?, ?, ?, ?)";

    @Override
    public void add(Long userId, EventType eventType, OperationType operationType, Long entityId) {
        jdbcTemplate.update(SQL_QUERY_ADD_EVENT, userId, TIMESTAMP,
                eventType.toString(), operationType.toString(), entityId);
    }
}
