package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
public class Film {
    Long id;

    @NotBlank(message = "Film name must not be null or blank.")
    String name;

    @Size(max = 200, message = "Film description must not exceed 200 characters.")
    String description;

    @NotNull(message = "Release date is required.")
    LocalDate releaseDate;

    @Positive(message = "Film duration must be greater than 0.")
    int duration;
}