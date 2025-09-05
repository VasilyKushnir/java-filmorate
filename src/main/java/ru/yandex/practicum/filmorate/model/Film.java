package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    private Long id;

    @NotBlank(message = "Film name must not be null or blank")
    private String name;

    private Set<String> genre;

    @Size(max = 200, message = "Film description must not exceed 200 characters")
    private String description;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    @Positive(message = "Film duration must be greater than 0")
    private int duration;

    private String MPA;

    private Set<Long> usersIdsLikes;
}