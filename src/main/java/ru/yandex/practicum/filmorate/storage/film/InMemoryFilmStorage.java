package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmMap = new HashMap<>();

    public Collection<Film> findAll() {
        return List.copyOf(filmMap.values());
    }

    public Film create(Film film) {
        Film newFilm = film.toBuilder().id(getNextId()).build();

        filmMap.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    public Film update(Film film) {
        filmMap.put(film.getId(), film);
        return film;
    }

    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(filmMap.get(id));
    }

    private long getNextId() {
        long currentMaxId = filmMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
