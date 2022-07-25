package ru.yandex.practicum.filmorate.storage.mapper;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.Event;
import ru.yandex.practicum.filmorate.model.entity.EventType;
import ru.yandex.practicum.filmorate.model.entity.OperationType;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getLong("event_id"));
        event.setUserId(rs.getLong("user_id"));
        event.setTimestamp(rs.getTimestamp("event_time").toInstant().toEpochMilli());
        event.setEventType(EventType.valueOf(rs.getString("event_type")));
        event.setOperation(OperationType.valueOf(rs.getString("operation_type")));
        event.setEntityId(rs.getLong("entity_id"));
        return event;
    }
}
