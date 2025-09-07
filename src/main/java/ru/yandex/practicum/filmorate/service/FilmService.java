package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final MpaService mpaService;
    private final GenreService genreService;

    public Collection<FilmDto> findAll() {
        return filmStorage.findAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public FilmDto getFilm(Long filmId) {
        return filmStorage.getFilm(filmId)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Film with id = " + filmId + " was not found"));
    }

    public FilmDto add(NewFilmRequest request) {
        Film film = FilmMapper.mapToFilm(request);
        validateFilm(film);
        film = filmStorage.add(film);
        return FilmMapper.mapToFilmDto(film);
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidationException("Release date cannot be earlier than December 28, 1895");
        }
        if (film.getMpa() != null && !mpaService.isMpaExists(film.getMpa().getId())) {
            throw new NotFoundException("Film MPA with id = " + film.getMpa().getId() + " is not exist");
        }
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (!genreService.isGenreExist(genre.getId())) {
                    throw new NotFoundException("Film genre with id = " + genre.getId() + " is not exist");
                }
            }
        }
    }

    public FilmDto update(UpdateFilmRequest request) {
        Film updatedFilm = filmStorage.getFilm(request.getId())
                .map(film -> FilmMapper.updateFilmFields(film, request))
                .orElseThrow(() -> new NotFoundException("Film was not found"));
        updatedFilm = filmStorage.update(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public boolean isFilmExist(Long filmId) {
        return filmStorage.getFilm(filmId).isPresent();
    }

    public List<FilmDto> fetchMostPopular(Integer count) {
        if (count <= 0) {
            throw new ValidationException("Films count must be bigger than zero");
        }
        return filmStorage.fetchMostPopular(count)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}