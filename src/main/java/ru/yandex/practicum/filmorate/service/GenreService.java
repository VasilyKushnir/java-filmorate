package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<GenreDto> getAll() {
        return genreStorage.findAll()
                .stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }

    public GenreDto getGenre(Integer genreId) {
        return genreStorage.findGenre(genreId)
                .map(GenreMapper::mapToGenreDto)
                .orElseThrow(() -> new NotFoundException("Genre with id = " + genreId + " was not found"));
    }

    public Set<Genre> getGenresForFilm(Long filmId) {
        return genreStorage.findGenresForFilm(filmId);
    }

    public boolean isGenreExist(Integer genreId) {
        return genreStorage.findGenre(genreId).isPresent();
    }

    public boolean isGenresExist(Collection<Genre> genres) {
        if (genres != null) {
            Collection<Integer> genresIds = genreStorage.findAll()
                    .stream()
                    .map(Genre::getId)
                    .toList();
            for (Genre genre : genres) {
                if (!genresIds.contains(genre.getId())) {
                    return false;
                }
            }
        }
        return true;
    }
}