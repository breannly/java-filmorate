package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<User> findAll() {
        return service.findAll();
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        return service.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return service.update(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long userId) {
        return service.findUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        service.addFriend(userId, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        service.deleteFriend(userId, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> findFriends(@PathVariable("id") Long userId) {
        return service.findFriends(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> findMutualFriends(@PathVariable("id") Long userId, @PathVariable Long otherId) {
        return service.findMutualFriends(userId, otherId);
    }
}