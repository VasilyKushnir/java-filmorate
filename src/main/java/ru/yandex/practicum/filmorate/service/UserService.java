package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
         this.userStorage = userStorage;
    }

    public User addToFriends(Long user1Id, Long user2Id) {
        User user1 = userStorage.getUser(user1Id).orElseThrow(() ->
                new NotFoundException("User1 with ID=" + user1Id + " was not found"));
        User user2 = userStorage.getUser(user2Id).orElseThrow(() ->
                new NotFoundException("User2 with ID=" + user2Id + " was not found"));

        user1.getFriendsIds().add(user2Id);
        user2.getFriendsIds().add(user1Id);
        userStorage.update(user1);
        userStorage.update(user2);
        log.info("User with ID={} and user with ID={} are now friends. That's magic!", user1Id, user2Id);
        return user1;
    }

    public User removeFromFriends(Long user1Id, Long user2Id) {
        User user1 = userStorage.getUser(user1Id).orElseThrow(() ->
                new NotFoundException("User1 with ID=" + user1Id + " was not found"));
        User user2 = userStorage.getUser(user2Id).orElseThrow(() ->
                new NotFoundException("User2 with ID=" + user2Id + " was not found"));

        user1.getFriendsIds().remove(user2Id);
        user2.getFriendsIds().remove(user1Id);
        userStorage.update(user1);
        userStorage.update(user2);
        log.info("User with ID={} and user with ID={} are no longer friends.", user1Id, user2Id);
        return user1;
    }

    public List<User> fetchFriendsList(Long userId) {
        User user = userStorage.getUser(userId).orElseThrow(() ->
                new NotFoundException("User with ID=" + userId + " was not found"));
        Set<Long> friendsIds = user.getFriendsIds();

        log.info("List of friends of user with ID={} was returned", userId);
        return friendsIds.stream()
                .map(n -> userStorage.getUser(n))
                .filter(o -> o.isPresent())
                .map(o -> o.get())
                .toList();
    }

    public List<User> fetchCommonFriendsList(Long user1Id, Long user2Id) {
        User user1 = userStorage.getUser(user1Id).orElseThrow(() ->
                new NotFoundException("User1 with ID=" + user1Id + " was not found"));
        User user2 = userStorage.getUser(user2Id).orElseThrow(() ->
                new NotFoundException("User2 with ID=" + user2Id + " was not found"));
        Set<Long> user1FriendsIds = user1.getFriendsIds();
        Set<Long> user2FriendsIds = user2.getFriendsIds();

        log.info("List of common friends between user with ID={} and user with ID={} was returned", user1Id, user2Id);
        return user1FriendsIds.stream()
                .filter(n -> user2FriendsIds.contains(n))
                .map(n -> userStorage.getUser(n))
                .filter(o -> o.isPresent())
                .map(o -> o.get())
                .toList();
    }
}