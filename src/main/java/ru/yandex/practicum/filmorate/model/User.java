package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends Entity {
    @Email(message = "Email недействительный")
    private String email;

    @NotBlank(message = "Логин пустой")
    private String login;

    @NotNull
    private String name;

    @PastOrPresent(message = "Дата рождения в будущем")
    private LocalDate birthday;
}
