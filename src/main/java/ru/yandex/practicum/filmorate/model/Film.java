package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Название не может быть пустым")
    private final String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private final String description;

    @NotNull(message = "Дата релиза обязательна")
    private final LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private final long duration;

    @Builder.Default
    private final Set<Long> likes = new HashSet<>();

    @AssertTrue(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    public boolean isReleaseDateValid() {
        if (releaseDate == null) return true;
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return !releaseDate.isBefore(minDate);
    }

    public Film addLike(Long friendId) {
        Set<Long> currentLikes = this.likes != null ? this.likes : new HashSet<>();
        Set<Long> updatedLikes = new HashSet<>(currentLikes);
        updatedLikes.add(friendId);

        return this.toBuilder()
                .likes(updatedLikes)
                .build();
    }

    public Film removeLike(Long friendId) {
        Set<Long> currentLikes = this.likes != null ? this.likes : new HashSet<>();
        Set<Long> updatedLikes = new HashSet<>(currentLikes);
        updatedLikes.remove(friendId);

        return this.toBuilder()
                .likes(updatedLikes)
                .build();
    }
}