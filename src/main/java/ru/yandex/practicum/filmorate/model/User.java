package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"email"})
@Builder(toBuilder = true)
public class User {
    private final long id;

    //@NotBlank(message = "Электронная почта не может быть пустой")
    //@Email(message = "Электронная почта должна содержать символ @ и быть валидной")
    private final String email;

    //@NotBlank(message = "Логин не может быть пустым")
    //@Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    private final String login;

    @Getter(AccessLevel.NONE)
    private final String name;

    //@Past(message = "Дата рождения не может быть в будущем")
    private final LocalDate birthday;

    public String getName() {
        return name != null && !name.trim().isEmpty() ? name : login;
    }
}