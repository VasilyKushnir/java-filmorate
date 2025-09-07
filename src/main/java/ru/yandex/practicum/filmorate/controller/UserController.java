package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.service.UserService;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FriendsService friendsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable("id") Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid NewUserRequest userRequest) {
        return userService.create(userRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@RequestBody @Valid UpdateUserRequest request) {
        return userService.update(request);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        friendsService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        friendsService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> fetchFriendsList(@PathVariable Long id) {
        return userService.fetchFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> fetchCommonFriendsList(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.fetchCommonFriendsList(id, otherId);
    }
}