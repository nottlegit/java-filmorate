package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

    public boolean isReleaseDateValid() {
        if (releaseDate == null) return true;
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return !releaseDate.isBefore(minDate);
    }
}