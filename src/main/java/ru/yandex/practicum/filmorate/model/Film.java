package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Duration;
import java.time.Instant;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Film {
    private final Long id;
    private final String name;
    private final String description;
    private final Instant releaseDate;
    private final Duration duration;
}
