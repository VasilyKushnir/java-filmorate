package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
public class User {
    Long id;

    @Email(message = "Invalid email format.")
    String email;

    @NotBlank(message = "Login must not be blank.")
    @Pattern(regexp = "^\\S+$", message = "Login must not contain spaces.")
    String login;

    String name;

    @PastOrPresent(message = "Birthday must be in the past or today.")
    LocalDate birthday;
}