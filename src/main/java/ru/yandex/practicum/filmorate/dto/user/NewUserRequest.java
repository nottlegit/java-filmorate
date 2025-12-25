package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserRequest {
    private String login;
    private String name;
    private String email;
    private LocalDate birthday;
}
