package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public Collection<UserDto> findAll() {
        return userStorage.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto getUser(Long userId) {
        return userStorage.getUser(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " was not found"));
    }

    public UserDto create(NewUserRequest request) {
        User user = UserMapper.mapToUser(request);
        user = userStorage.create(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UpdateUserRequest request) {
        User updatedUser = userStorage.getUser(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("User was not found"));
        updatedUser = userStorage.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public boolean isUserExist(Long userId) {
        return userStorage.getUser(userId).isPresent();
    }

    public List<UserDto> fetchFriendsList(Long userId) {
        if (!isUserExist(userId)) {
            throw new NotFoundException("User with ID=" + userId + " was not found");
        }
        return userStorage.fetchFriendsList(userId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public List<UserDto> fetchCommonFriendsList(Long user1Id, Long user2Id) {
        User user1 = userStorage.getUser(user1Id).orElseThrow(() ->
                new NotFoundException("User1 with ID=" + user1Id + " was not found"));
        User user2 = userStorage.getUser(user2Id).orElseThrow(() ->
                new NotFoundException("User2 with ID=" + user2Id + " was not found"));
        return userStorage.fetchCommonFriendsList(user1Id, user2Id)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }
}