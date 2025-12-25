package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static User mapToUser(NewUserRequest request) {
        return User.builder()
                .name(request.getName())
                .login(request.getLogin())
                .email(request.getEmail())
                .birthday(request.getBirthday()).build();
    }

    public static UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setBirthday(user.getBirthday());
        return dto;
    }

    /*public static User updateUserFields(User user, UpdateUserRequest request) {
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasPassword()) {
            user.setPassword(request.getPassword());
        }
        if (request.hasUsername()) {
            user.setUsername(request.getUsername());
        }
        return user;
    }*/
}