package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос на получение всех пользователей.");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@Valid @PathVariable long id) {
        log.info("Получен запрос на получение пользователя с id = {}", id);

        User findedUser = userService.findById(id);
        log.info("Пользователь успешно найден: {}", findedUser);
        return findedUser;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание нового пользователя: {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя: {}", user);
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info(
                "Получен запрос на добавление в друзья: пользователь id = {} добавляет пользователя id = {}",
                id, friendId
        );

        userService.addFriend(id, friendId);
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
    public Collection<User> getFriends(@PathVariable long id) {
        log.info("Получен запрос на получение друзей: пользователь id = {}", id);

        Collection<User> friends = userService.getFriendsByUserId(id);

        log.info("Возвращено {} друзей пользователя {}", friends.size(), id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Получен запрос на получение общих друзей: пользователь id = {} с пользователем id = {}", id, otherId);

        Collection<User> friends = userService.getMutualFriends(id, otherId);
        log.info("Возвращено {} друзей", friends.size());
        return friends;
    }
}