package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

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
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {
        User.UserBuilder builder = user.toBuilder();

        if (request.hasEmail()) {
            builder.email(request.getEmail());
        }
        if (request.hasLogin()) {
            builder.login(request.getLogin());
        }
        if (request.hasName()) {
            builder.name(request.getName());
        }
        if (request.hasBirthday()) {
            builder.birthday(request.getBirthday());
        }

        return builder.build();
    }
}