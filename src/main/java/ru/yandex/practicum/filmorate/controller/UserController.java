package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController("/users")
public class UserController {
    List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        users.add(user);

        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (users.contains(user)) {
            users.remove(user);
        }
        users.add(user);

        return user;
    }
}
