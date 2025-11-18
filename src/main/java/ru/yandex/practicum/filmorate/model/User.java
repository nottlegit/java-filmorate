package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = {"email"})
public class User {
    private final long id;
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;
}
