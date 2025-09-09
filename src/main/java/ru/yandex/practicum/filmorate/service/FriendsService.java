package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendsService {
    private final FriendsStorage friendsStorage;
    private final UserService userService;

    public void getFriend(Long offeringPersonId, Long recipientPersonId) {
        if (userService.isUserExist(offeringPersonId) && userService.isUserExist(recipientPersonId)) {
            friendsStorage.addFriend(offeringPersonId, recipientPersonId);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public void removeFriend(Long offeringPersonId, Long recipientPersonId) {
        if (userService.isUserExist(offeringPersonId) && userService.isUserExist(recipientPersonId)) {
            friendsStorage.removeFriend(offeringPersonId, recipientPersonId);
        } else {
            throw new NotFoundException("User not found");
        }
    }
}