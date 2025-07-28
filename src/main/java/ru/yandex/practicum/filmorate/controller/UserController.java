package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        if (user == null) {
            throw new ValidationException("User must not be null.");
        }
        user.setId(getNextId());
        fillNameIfBlank(user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        if (user.getId() == null) {
            throw new ValidationException("User ID must not be null for update.");
        }
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("User with ID " + user.getId() + " does not exist.");
        }
        User currentUser = users.get(user.getId());
        currentUser.setEmail(user.getEmail());
        currentUser.setLogin(user.getLogin());
        fillNameIfBlank(user);
        currentUser.setName(user.getName());
        currentUser.setBirthday(user.getBirthday());
        return currentUser;
    }

    private void fillNameIfBlank(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private long getNextId() {
        return users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}