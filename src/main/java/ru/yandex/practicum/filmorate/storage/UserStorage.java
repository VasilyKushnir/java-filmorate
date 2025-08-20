package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    public Collection<User> findAll();

    public Optional<User> getUser(Long userId);

    public User create(User user);

    public User update(User user);
}