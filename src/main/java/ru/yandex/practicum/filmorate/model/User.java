package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class User extends IdEntity {
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
