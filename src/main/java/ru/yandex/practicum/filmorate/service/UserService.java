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
        log.info("Обновление пользователя id={}.", request.getId());

        User updatedUser = userRepository.findById(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        updatedUser = userRepository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", userId)
                ));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", friendId)
                ));

        Optional<Friendship> existingFriendship = friendshipRepository.findByIds(user.getId(), friend.getId());
        if (existingFriendship.isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Пользователь %d уже дружит с пользователем %d", userId, friendId)
            );
        }

        Friendship friendship = Friendship.builder()
                .userId(userId)
                .friendId(friendId)
                .status(FriendshipStatus.PENDING)
                .build();

        friendship = friendshipRepository.save(friendship);

        log.info("Пользователь {} успешно добавил в друзья {}", friendship.getUserId(), friendship.getFriendId());
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", userId)
                ));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", friendId)
                ));

        Optional<Friendship> friendshipOptional = friendshipRepository.findByIds(user.getId(), friend.getId());
        if (friendshipOptional.isEmpty()) {
            return;
        }

        if(friendshipRepository.deleteByIds(userId, friendId)) {
            log.info("Пользователь {} успешно удалил из друзей {}", userId, friendId);
        }
    }

    public Collection<UserDto> getFriendsByUserId(long userId) {
        if (userId < 0) {
            throw new ValidationException("ID пользователя должен быть положительным");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", userId)
                ));

        Collection<Friendship> friendships = friendshipRepository.findByUserId(user.getId());

        return friendships.stream()
                .map(friendship -> userRepository.findById(friendship.getFriendId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public Collection<UserDto> getMutualFriends(long userId, long otherUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", userId)
                ));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", otherUserId)
                ));

        List<Long> userFriendIds = friendshipRepository.findByUserId(user.getId()).stream()
                .map(Friendship::getFriendId)
                .toList();

        List<Long> otherUserFriendIds = friendshipRepository.findByUserId(otherUser.getId()).stream()
                .map(Friendship::getFriendId)
                .toList();

        List<Long> mutualFriendIds = userFriendIds.stream()
                .filter(otherUserFriendIds::contains)
                .toList();

        if (mutualFriendIds.isEmpty()) {
            return Collections.emptyList();
        }

        return mutualFriendIds.stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }
}