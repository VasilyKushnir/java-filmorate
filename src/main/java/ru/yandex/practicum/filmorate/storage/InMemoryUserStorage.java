package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        log.info("Received request to retrieve all users. Total users: {}", users.size());
        return new ArrayList<>(users.values());
    }

    public Optional<User> getUser(Long userId) {
        if (userId <= 0) {
            log.warn("Received request to get user with invalid ID={}", userId);
            throw new ValidationException("User ID must be positive number");
        }
        log.info("Fetching user with ID={}", userId);
        return Optional.ofNullable(users.get(userId));
    }

    public User create(User user) {
        if (user == null) {
            log.warn("Received null user in POST /users request");
            throw new ValidationException("User must not be null");
        }
        user.setId(getNextId());
        fillNameIfBlank(user);
        user.setFriendsIds(new HashSet<>());
        users.put(user.getId(), user);
        log.info("User created successfully: {}", user.toString());
        return user;
    }

    public User update(User user) {
        if (user.getId() == null) {
            log.warn("Received user update request without ID");
            throw new NotFoundException("User ID must not be null for update");
        }
        if (!users.containsKey(user.getId())) {
            log.warn("Update failed: user with ID={} not found", user.getId());
            throw new NotFoundException("User with ID=" + user.getId() + " does not exist");
        }
        User currentUser = users.get(user.getId());
        currentUser.setEmail(user.getEmail());
        currentUser.setLogin(user.getLogin());
        fillNameIfBlank(user);
        currentUser.setName(user.getName());
        currentUser.setBirthday(user.getBirthday());
        log.info("User updated successfully: {}", currentUser.toString());
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