package ru.yandex.practicum.filmorate.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class IdEntity {
    @EqualsAndHashCode.Include
    private Long id;
}
