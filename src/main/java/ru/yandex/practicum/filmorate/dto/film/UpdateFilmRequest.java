package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateFilmRequest {
    private final long id;

    private final String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private final String description;

    private final LocalDate releaseDate;
    private final Long duration;

    @JsonProperty("mpa")
    private Mpa mpa;

    private Set<Genre> genres;

    @AssertTrue(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    public boolean isReleaseDateValid() {
        if (releaseDate == null) return true;
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return !releaseDate.isBefore(minDate);
    }

    @AssertTrue(message = "Продолжительность фильма должна быть положительным числом")
    public boolean isDurationPositiveOrNull() {
        if (description == null) return true;
        return !(duration <= 0);
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasMpaRatingId() {
        return mpa != null;
    }

    public boolean hasGenre() {
        return genres != null && !genres.isEmpty();
    }
}