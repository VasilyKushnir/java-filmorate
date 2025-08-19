package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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
//        return filmStorage.findAll();
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Work in progress");
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
//        return filmStorage.add(film);
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Work in progress");
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
//        return filmStorage.update(film);
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Work in progress");
    }
}