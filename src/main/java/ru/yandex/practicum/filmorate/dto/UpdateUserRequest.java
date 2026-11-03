package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    @NotNull(message = "Id must not be null")
    private Long id;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Login must not be blank")
    @Pattern(regexp = "^\\S+$", message = "Login must not contain spaces")
    private String login;

    private String name;

    @PastOrPresent(message = "Birthday must be in the past or today")
    private LocalDate birthday;

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }

    public boolean hasLogin() {
        return !(login == null || login.isBlank());
    }

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasBirthday() {
        return birthday != null;
    }
}