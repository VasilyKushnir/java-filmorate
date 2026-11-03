package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikesService {
    private final LikesStorage likesStorage;
    private final UserService userService;
    private final FilmService filmService;

    public void addLike(Long filmId, Long userId) {
        if (userService.isUserExist(userId) && filmService.isFilmExist(filmId)) {
            likesStorage.addLike(userId, filmId);
        } else {
            throw new NotFoundException("Not found");
        }
    }

    public void removeLike(Long filmId, Long userId) {
        if (userService.isUserExist(userId) && filmService.isFilmExist(filmId)) {
            likesStorage.removeLike(userId, filmId);
        } else {
            throw new NotFoundException("Not found");
        }
    }
}