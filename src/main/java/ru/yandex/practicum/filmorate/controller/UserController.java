package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getUsers() {
        log.info("Получен запрос на получение всех пользователей.");
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@Valid @PathVariable long userId) {
        log.info("Получен запрос на получение пользователя с id = {}", userId);

        UserDto userDto = userService.getUserById(userId);
        log.info("Пользователь успешно найден: {}", userDto);
        return userDto;
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody NewUserRequest userRequest) {
        log.info("Получен запрос на создание нового пользователя: {}", userRequest);
        return userService.createUser(userRequest);
    }

    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        log.info("Получен запрос на обновление пользователя: {}", updateUserRequest);
        return userService.updateUser(updateUserRequest);
    }
    /// /////////////////////////////////////////////////////////

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.info(
                "Получен запрос на добавление в друзья: пользователь id = {} добавляет пользователя id = {}",
                userId, friendId
        );

        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info(
                "Получен запрос на удаление из друзей: пользователь id = {} удаляет пользователя id = {}",
                id, friendId
        );
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> getFriends(@PathVariable long id) {
        log.info("Получен запрос на получение друзей: пользователь id = {}", id);

        Collection<UserDto> friends = userService.getFriendsByUserId(id);

        log.info("Возвращено {} друзей пользователя {}", friends.size(), id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDto> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Получен запрос на получение общих друзей: пользователь id = {} с пользователем id = {}", id, otherId);

        Collection<UserDto> friends = userService.getMutualFriends(id, otherId);
        log.info("Возвращено {} друзей", friends.size());
        return friends;
    }
}