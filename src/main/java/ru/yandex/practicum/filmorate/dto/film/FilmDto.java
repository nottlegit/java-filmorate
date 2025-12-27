package ru.yandex.practicum.filmorate.dto.film;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class FilmDto {
    private final long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final Long duration;
    private Mpa mpa;

    @Builder.Default
    private Set<Genre> genres = new HashSet<>();
}
