package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.time.LocalDate;
import java.time.Month;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Received request to retrieve all films. Total films: {}.", films.size());
        return films.values();
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        if (film == null) {
            log.warn("Received null film in POST /films request.");
            throw new ValidationException("Film must not be null.");
        }
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Film added successfully: {}", film.toString());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        if (film.getId() == null) {
            log.warn("Received film update request without ID.");
            throw new ValidationException("Film ID must not be null for update.");
        }
        if (!films.containsKey(film.getId())) {
            log.warn("Update failed: film with ID {} not found.", film.getId());
            throw new ValidationException("Film with ID " + film.getId() + " does not exist.");
        }
        validateFilm(film);
        Film currentFilm = films.get(film.getId());
        currentFilm.setName(film.getName());
        currentFilm.setDescription(film.getDescription());
        currentFilm.setReleaseDate(film.getReleaseDate());
        currentFilm.setDuration(film.getDuration());
        log.info("Film updated successfully: {}", currentFilm.toString());
        return currentFilm;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidationException("Release date cannot be earlier than December 28, 1895.");
        }
    }

    private long getNextId() {
        return films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}