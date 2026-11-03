package ru.yandex.practicum.filmorate.storage;

public interface LikesStorage {
    void addLike(Long userId, Long filmId);

    void removeLike(Long userId, Long filmId);
}