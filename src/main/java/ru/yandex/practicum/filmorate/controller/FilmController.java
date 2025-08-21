package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.service.FilmService;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Long filmId) {
        return filmService.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Film with id = " + filmId + " was not found"));
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> fetchMostPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.fetchMostPopular(count);
    }
}