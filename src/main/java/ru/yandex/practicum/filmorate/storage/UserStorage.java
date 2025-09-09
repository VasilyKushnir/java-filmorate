package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAll();

    Optional<User> findUser(Long userId);

    User create(User user);

    User update(User user);

    List<User> findFriendsList(Long userId);

    List<User> findCommonFriendsList(Long offeringPersonId, Long recipientPersonId);
}