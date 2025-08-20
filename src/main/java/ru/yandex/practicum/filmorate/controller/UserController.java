package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.storage.UserStorage;
import jakarta.validation.Valid;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    UserStorage userStorage;

    @Autowired
    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<User> findAll() {
         return userStorage.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long userId) {
        return userStorage.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " was not found"));
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        return userStorage.update(user);
    }
}