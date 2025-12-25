package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.user.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.exception.IllegalArgumentException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Collection<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(long userId) {
        if (userId < 0) {
            throw new ValidationException("ID пользователя должен быть положительным");
        }

        return userRepository.findById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }

    public UserDto createUser(NewUserRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        } // нужно ли это?

        Optional<User> alreadyExistUser = userRepository.findByEmail(request.getEmail());
        if (alreadyExistUser.isPresent()) {
            throw new DuplicatedDataException("Данный имейл уже используется");
        }

        User user = UserMapper.mapToUser(request);

        user = userRepository.save(user);
        log.info("Пользователь успешно создан: {}", user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto updateUser(UpdateUserRequest request) {
        // проверить, что нельзя обновить пользователя, если изменилась почта, и она уже кем-то исп.
        log.info("Обновление пользователя id={}.", request.getId());

        User updatedUser = userRepository.findById(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        updatedUser = userRepository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }
    /// /////////////////////////////////////////////////////////////////////////////

    /*public void addFriend(long userId, long friendId) {
        if (userId < 0 || friendId < 0) {
            throw new ValidationException("ID пользователя должен быть положительным");
        }

        if (userId == friendId) {
            throw new IllegalArgumentException(
                    String.format("Пользователь с id=%d не может добавить себя в друзья", userId)
            );
        }

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


        log.info("Пользователь {} успешно удалил из друзей {}", userId, friendId);
    }

    public Collection<User> getFriendsByUserId(long id) {

    }

    public Collection<User> getMutualFriends(long id, long otherId) {

    }*/
}