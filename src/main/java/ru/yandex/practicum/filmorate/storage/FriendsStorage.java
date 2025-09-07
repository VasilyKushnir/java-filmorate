package ru.yandex.practicum.filmorate.storage;

public interface FriendsStorage {
    void addFriend(Long offeringPersonId, Long recipientPersonId);

    void removeFriend(Long offeringPersonId, Long recipientPersonId);
}