package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.exception.IllegalArgumentException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.friendship.Friendship;
import ru.yandex.practicum.filmorate.model.friendship.FriendshipStatus;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public Collection<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(long userId) {
        if (userId < 0) {
            throw new ValidationException("ID пользователя должен быть положительным");
        }
        return userRepository.findById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + userId));
    }

    public UserDto createUser(NewUserRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new DuplicatedDataException("Данный имейл уже используется");
        });

        User user = UserMapper.mapToUser(request);
        user = userRepository.save(user);
        log.info("Пользователь создан: {}", user.getId());
        return UserMapper.mapToUserDto(user);
    }

    public UserDto updateUser(UpdateUserRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        User updatedUser = userRepository.update(UserMapper.updateUserFields(user, request));
        return UserMapper.mapToUserDto(updatedUser);
    }

    public void addFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new IllegalArgumentException("Нельзя добавить самого себя в друзья");
        }

        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id=%d не найден", userId)));
        userRepository.findById(friendId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id=%d не найден", friendId)));

        if (friendshipRepository.findByIds(userId, friendId).isPresent()) {
            throw new IllegalArgumentException(String.format("Пользователь с id %d уже дружит в пользователем id %d",
                    userId, friendId));
        }

        friendshipRepository.save(Friendship.builder()
                .userId(userId)
                .friendId(friendId)
                .status(FriendshipStatus.PENDING)
                .build());
    }

    public void deleteFriend(long userId, long friendId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("Пользователь с id=%d не найден", userId
        )));
        userRepository.findById(friendId).orElseThrow(() -> new NotFoundException(
                String.format("Пользователь с id=%d не найден", friendId
        )));
        friendshipRepository.deleteByIds(userId, friendId);
    }

    public Collection<UserDto> getFriendsByUserId(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Long> friendIds = friendshipRepository.findByUserId(userId).stream()
                .map(Friendship::getFriendId)
                .toList();

        return userRepository.findByIds(friendIds).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public Collection<UserDto> getMutualFriends(long userId, long otherUserId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("Пользователь с id=%d не найден", userId)
        ));
        userRepository.findById(otherUserId).orElseThrow(() -> new NotFoundException(
                String.format("Пользователь с id=%d не найден", otherUserId)
        ));

        Set<Long> userFriends = friendshipRepository.findByUserId(userId).stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet());

        Set<Long> otherFriends = friendshipRepository.findByUserId(otherUserId).stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet());

        userFriends.retainAll(otherFriends);

        return userRepository.findByIds(userFriends).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }
}
