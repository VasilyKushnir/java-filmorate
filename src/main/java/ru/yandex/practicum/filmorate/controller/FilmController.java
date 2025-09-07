package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.service.FilmService;
import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.service.LikesService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final LikesService likesService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDto> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto getFilm(@PathVariable("id") Long filmId) {
        return filmService.getFilm(filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto add(@RequestBody @Valid NewFilmRequest filmRequest) {
        return filmService.add(filmRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmDto update(@RequestBody @Valid UpdateFilmRequest request) {
        return filmService.update(request);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        likesService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        likesService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDto> fetchMostPopular(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.fetchMostPopular(count);
    }
}