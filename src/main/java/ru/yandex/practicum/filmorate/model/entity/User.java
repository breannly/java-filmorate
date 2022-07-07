package ru.yandex.practicum.filmorate.model.entity;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class User extends IdEntity {
    @Email(message = "Email недействительный")
    private String email;

    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
