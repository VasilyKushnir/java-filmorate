package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private Long id;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Login must not be blank")
    @Pattern(regexp = "^\\S+$", message = "Login must not contain spaces")
    private String login;

    private String name;

    @PastOrPresent(message = "Birthday must be in the past or today")
    private LocalDate birthday;

    private Set<Long> friendsIds;
}