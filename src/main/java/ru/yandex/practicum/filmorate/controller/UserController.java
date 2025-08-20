package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    UserStorage userStorage;
    UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
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

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> fetchFriendsList(@PathVariable Long id) {
        return userService.fetchFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> fetchCommonFriendsList(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.fetchCommonFriendsList(id, otherId);
    }
}