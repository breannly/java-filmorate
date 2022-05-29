package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerTest {
    private final UserController controller;

    public UserControllerTest() {
        controller = new UserController();
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginHasWhitespace() {
        String wrongLogin = "test test";
        User user = getDefaultUser();
        user.setLogin(wrongLogin);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> controller.add(user)
        );
        Assertions.assertEquals("Пользователь не соответствует критериям", exception.getMessage());
    }

    @Test
    void shouldChangeNameToLoginWhenNameEmpty() throws ValidationException {
        String wrongName = "";
        User user = getDefaultUser();
        user.setName(wrongName);
        controller.add(user);

        Assertions.assertEquals(user.getName(), user.getLogin());
    }

    private User getDefaultUser() {
        User user = new User("test@email.com",
                "test",
                "test",
                LocalDate.of(2003, 06, 10));

        return user;
    }
}