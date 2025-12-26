package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {
    public static Film mapToFilm(NewFilmRequest request) {
        return Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpa(request.getMpa())
                .build();
    }

    public static FilmDto mapToFilmDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(film.getMpa())
                .build();
    }

    public static Film updateUserFields(Film film, UpdateFilmRequest request) {
        Film.FilmBuilder builder = film.toBuilder();

        if (request.hasName()) {
            builder.name(request.getName());
        }
        if (request.hasDescription()) {
            builder.description(request.getDescription());
        }
        if(request.hasReleaseDate()) {
            builder.releaseDate(request.getReleaseDate());
        }
        if(request.hasDuration()) {
            builder.duration(request.getDuration());
        }
        if(request.hasMpaRatingId()) {
            builder.mpa(request.getMpa());
        }

        return builder.build();
    }
}
