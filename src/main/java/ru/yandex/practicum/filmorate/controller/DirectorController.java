package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.entity.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorsService;

    @GetMapping
    public List<Director> findAll() {
        return directorsService.findAll();
    }

    @GetMapping("/{id}")
    public Director findDirectorById(@PathVariable("id") Long directorId) {
        return directorsService.findById(directorId);
    }

    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director) {
        return directorsService.add(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorsService.update(director);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long directorId){
        directorsService.deleteDirector(directorId);
    }
}
