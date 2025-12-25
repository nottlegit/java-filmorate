package ru.yandex.practicum.filmorate.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String login;
    private String name;
    private String email;
    private LocalDate birthday;
}
