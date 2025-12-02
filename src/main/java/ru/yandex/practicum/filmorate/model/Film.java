package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Film {
    private final long id;

    @NotBlank(message = "Название не может быть пустым")
    private final String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private final String description;

    @NotNull(message = "Дата релиза обязательна")
    private final LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private final long duration;

    @AssertTrue(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    public boolean isReleaseDateValid() {
        if (releaseDate == null) return true;
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return !releaseDate.isBefore(minDate);
    }
}