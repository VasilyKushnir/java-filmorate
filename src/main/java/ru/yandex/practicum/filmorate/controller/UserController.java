package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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
//        return userStorage.findAll();
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Work in progress");
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
//        return userStorage.create();
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Work in progress");
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
//        return userStorage.update();
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Work in progress");
    }
}