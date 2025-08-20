package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    public Collection<Film> findAll();

    public Optional<Film> getFilm(Long filmId);

    public Film add(Film film);

    public Film update(Film film);
}