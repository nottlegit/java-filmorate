package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

@UtilityClass
public final class FilmMapper {
    public static Film mapToFilm(NewFilmRequest request) {
        return Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpa(request.getMpa())
                .genres(request.getGenres())
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
                .genres(film.getGenres())
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
        if (request.hasReleaseDate()) {
            builder.releaseDate(request.getReleaseDate());
        }
        if (request.hasDuration()) {
            builder.duration(request.getDuration());
        }
        if (request.hasMpaRatingId()) {
            builder.mpa(request.getMpa());
        }
        if (request.hasGenre()) {
            builder.genres(request.getGenres());
        }

        return builder.build();
    }
}