package ru.yandex.practicum.filmorate.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Director extends IdEntity {

    @NotBlank(message = "Имя не должен быть пустым")
    private String name;
}
