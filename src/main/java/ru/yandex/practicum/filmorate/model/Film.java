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
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final long duration;
    private Mpa mpa;

    public boolean isReleaseDateValid() {
        if (releaseDate == null) return true;
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return !releaseDate.isBefore(minDate);
    }
}