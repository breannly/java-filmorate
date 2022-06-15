package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<User> findAll() {
        return service.findAll();
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        validate(user);
        return service.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validate(user);
        return service.update(user);
    }

    private void validate(User user) {
        boolean isWrongName = user.getName().isBlank();

        if (isWrongName) {
            log.info("У пользователя {} user изменено имя на {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return service.findUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        return service.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return service.getMutualFriends(id, otherId);
    }
}
