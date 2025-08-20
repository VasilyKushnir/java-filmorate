package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId).orElseThrow(() ->
                new NotFoundException("Film with ID=" + filmId + " was not found"));
        if (userStorage.hasUser(userId)) {
            film.getUsersIdsLikes().add(userId);
            filmStorage.update(film);
            log.info("Film with ID={} was liked by user with ID={}", filmId, userId);
            return film;
        } else {
            throw new NotFoundException("User with ID=" + userId + " was not found");
        }
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId).orElseThrow(() ->
                new NotFoundException("Film with ID=" + filmId + " was not found"));
        if (userStorage.hasUser(userId)) {
            film.getUsersIdsLikes().remove(userId);
            filmStorage.update(film);
            log.info("Film with ID={} was no longer liked by user with ID={}", filmId, userId);
            return film;
        } else {
            throw new NotFoundException("User with ID=" + userId + " was not found");
        }
    }

    public List<Film> fetchMostPopular(int count) {
        if (count <= 0) {
            throw new ValidationException("Films count must be bigger than zero");
        }

        log.info("List of top ten films has returned");
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> f1.getUsersIdsLikes().size() - f2.getUsersIdsLikes().size())
                .limit(count)
                .toList().reversed();
    }
}