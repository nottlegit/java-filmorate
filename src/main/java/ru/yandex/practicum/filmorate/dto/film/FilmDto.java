package ru.yandex.practicum.filmorate.dto.film;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class FilmDto {
    private final long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final Long duration;
    private Mpa mpa;
}
