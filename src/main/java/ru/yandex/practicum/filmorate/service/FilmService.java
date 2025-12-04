package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage filmStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(long id) {
        if (id < 0) {
            throw new ValidationException("ID фильма должен быть положительным");
        }

        return filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Фильм с id=%d не найден", id)
                ));
    }

    public Film create(Film film) {
        Film createdFilm = filmStorage.create(film);

        log.info("Фильм успешно создан: {}", createdFilm);
        return createdFilm;
    }

    public Film update(Film film) {
        Film existingFilm = findById(film.getId());

        log.info("Обновление фильма id={}. Дата выхода {}",
                film.getId(), existingFilm.getReleaseDate());
        Film updatedFilm = filmStorage.update(film);

        log.info("Фильм успешно обновлен: {}", updatedFilm);
        return updatedFilm;
    }

    public void addLike(long filmId, long userId) {
        if (userId < 0 || filmId < 0) {
            throw new ValidationException("ID должен быть положительным");
        }

        Film film = findById(filmId).addLike(userId);

        filmStorage.update(film);

        log.info("Пользователь с id = {} успешно поставил лайк фильму с id = {}", userId, filmId);
    }

    public void deleteLike(long filmId, long userId) {
        if (userId < 0 || filmId < 0) {
            throw new ValidationException("ID должен быть положительным");
        }

        Film film = findById(filmId).removeLike(userId);

        filmStorage.update(film);

        log.info("Пользователь с id = {} успешно удалил лайк фильму с id = {}", userId, filmId);
    }

    public Collection<Film> getPriorityList(int count) {
        if (count < 0) {
            throw new ValidationException("Count должен быть положительным");
        }

        Collection<Film> filmsPriority = findAll().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .toList();

        log.info("Успешно получен список из первых фильмов по количеству лайков размером: {}", filmsPriority.size());
        return filmsPriority;
    }
}
