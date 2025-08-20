package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.storage.FilmStorage;
import jakarta.validation.Valid;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Long filmId) {
        return filmStorage.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Film with id = " + filmId + " was not found"));
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        return filmStorage.add(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return filmStorage.update(film);
    }
}