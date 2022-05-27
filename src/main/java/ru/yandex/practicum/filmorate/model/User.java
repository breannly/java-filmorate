package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {
    private Long id;

    @Email(message = "Email недействительный")
    private String email;

    @NotBlank(message = "Логин пустой")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения в будущем")
    private LocalDate birthday;
}
