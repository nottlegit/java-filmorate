package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"email"})
public class NewUserRequest {
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    private String login;

    @Getter(AccessLevel.NONE)
    private String name;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна содержать символ @ и быть валидной")
    private String email;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public String getName() {
        return name != null && !name.trim().isEmpty() ? name : login;
    }
}