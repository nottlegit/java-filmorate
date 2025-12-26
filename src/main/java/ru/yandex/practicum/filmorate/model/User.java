package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"email"})
@Builder(toBuilder = true)
public class User {
    private final long id;
    private final String email;
    private final String login;

    @Getter(AccessLevel.NONE)
    private final String name;

    private final LocalDate birthday;

    public String getName() {
        return name != null && !name.trim().isEmpty() ? name : login;
    }
}