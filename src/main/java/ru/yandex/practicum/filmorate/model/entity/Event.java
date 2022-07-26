package ru.yandex.practicum.filmorate.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
    Long eventId;
    Long timestamp;
    Long userId;
    EventType eventType;
    OperationType operation;
    Long entityId;
}
