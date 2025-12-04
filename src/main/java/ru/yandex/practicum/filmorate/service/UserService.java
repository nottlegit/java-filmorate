package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.exception.IllegalArgumentException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final InMemoryUserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(long id) {
        if (id < 0) {
            throw new ValidationException("ID пользователя должен быть положительным");
        }

        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", id)
                ));
    }

    public User create(User user) {
        User createdUser = userStorage.create(user);

        log.info("Пользователь успешно создан: {}", createdUser);
        return createdUser;
    }

    public User update(User user) {
        User existingUser = userStorage.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", user.getId())
                ));

        if (!existingUser.getEmail().equals(user.getEmail())) {
            boolean emailExists = userStorage.findAll().stream()
                    .anyMatch(u -> u.getEmail().equals(user.getEmail()));
            if (emailExists) {
                log.warn(
                        "Ошибка при обновлении пользователя: email уже используется другим пользователем {}",
                        user.getEmail()
                );
                throw new ValidationException("Email уже используется другим пользователем");
            }
        }

        log.info("Обновление пользователя id={}. Старый email: {}, новый email: {}",
                user.getId(), existingUser.getEmail(), user.getEmail());
        User updatedUser = userStorage.update(user);

        log.info("Пользователь успешно обновлен: {}", updatedUser);
        return updatedUser;
    }

    public void addFriend(long userId, long friendId) {
        if (userId < 0 || friendId < 0) {
            throw new ValidationException("ID пользователя должен быть положительным");
        }

        if (userId == friendId) {
            throw new IllegalArgumentException(
                    String.format("Пользователь с id=%d не может добавить себя в друзья", userId)
            );
        }

        User user = findById(userId);
        User friend = findById(friendId);

        if (user.getFriends().contains(friendId)) {
            throw new AlreadyFriendsException(userId, friendId);
        }

        User updatedUser = user.addFriend(friendId);
        User updatedFriend = friend.addFriend(userId);

        userStorage.update(updatedUser);
        userStorage.update(updatedFriend);
        log.info("Пользователь {} успешно добавил в друзья {}", userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        if (userId < 0 || friendId < 0) {
            throw new ValidationException("ID пользователя должен быть положительным");
        }

        if (userId == friendId) {
            throw new IllegalArgumentException(
                    String.format("Пользователь с id=%d не может удалить себя из друзей", userId)
            );
        }

        User user = findById(userId);
        User friend = findById(friendId);

        if (!user.getFriends().contains(friendId)) {
            throw new NotFriendsException(userId, friendId);
        }

        User updatedUser = user.removeFriend(friendId);
        User updatedFriend = friend.removeFriend(userId);

        userStorage.update(updatedUser);
        userStorage.update(updatedFriend);
        log.info("Пользователь {} успешно удалил из друзей {}", userId, friendId);
    }

    public Collection<User> getFriendsByUserId(long id) {
        return findById(id).getFriends()
                .stream()
                .map(friendId -> {
                    try {
                        return findById(friendId);
                    } catch (NotFoundException e) {
                        throw new InconsistentDataException (id, friendId, e);
                    }
                })
                .collect(Collectors.toList());
    }

    public Collection<User> getMutualFriends(long id, long otherId) {
        User user1 = findById(id);
        User user2 = findById(otherId);

        return user1.getFriends().stream()
                .filter(user2.getFriends()::contains)
                .map(friendId -> {
                    try {
                        return findById(friendId);
                    } catch (NotFoundException e) {
                        throw new InconsistentDataException (user1.getId(), friendId, e);
                    }
                })
                .collect(Collectors.toList());
    }
}
