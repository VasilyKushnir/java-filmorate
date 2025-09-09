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
public class NewFilmRequest {
    @NotNull(message = "Film name must not be null")
    @NotBlank(message = "Film name must not be blank")
    private String name;

    @NotNull(message = "Film description name must not be null")
    @Size(max = 200, message = "Film description must not exceed 200 characters")
    private String description;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    @NotNull(message = "Film duration must not be null")
    @Positive(message = "Film duration must be greater than 0")
    private Integer duration;

    private Set<Genre> genres;
    private Mpa mpa;
}