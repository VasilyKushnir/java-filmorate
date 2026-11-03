package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateFilmRequest {
    @NotNull(message = "Id must not be null")
    private Long id;

    @NotBlank(message = "Film name must not be blank")
    private String name;

    @Size(max = 200, message = "Film description must not exceed 200 characters")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Film duration must be greater than 0")
    private Integer duration;

    private Set<Genre> genres;
    private Mpa mpa;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasGenres() {
        return genres != null;
    }

    public boolean hasMpa() {
        return mpa != null;
    }
}