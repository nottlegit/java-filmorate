package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    /*private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public Collection<Film> findAll() {
        Collection<Film> collection = filmStorage.findAll();

        log.info("Успешно получены все фильмы. Текущее количество {}", collection.size());
        return collection;
    }

    public Film findById(long id) {
        if (id < 0) {
            String errorMessage = "ID фильма должен быть положительным";
            log.warn("Ошибка при обновлении фильма: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        Film findedFilm = filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Фильм с id = %d не найден", id)
                ));

        log.info("Фильм успешно получен по id: {}", findedFilm.getId());
        return findedFilm;
    }

    public Film create(Film film) {
        Film createdFilm = filmStorage.create(film);

        log.info("Фильм успешно добавлен: {}", createdFilm);
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

        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден, поставить лайк невозможно", userId)
                ));

        Film film = findById(filmId).addLike(userId);

        filmStorage.update(film);

        log.info("Пользователь с id = {} успешно поставил лайк фильму с id = {}", user.getId(), film.getId());
    }

    public void deleteLike(long filmId, long userId) {
        if (userId < 0 || filmId < 0) {
            throw new ValidationException("ID должен быть положительным");
        }

        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден, поставить лайк невозможно", userId)
                ));

        Film film = findById(filmId).removeLike(userId);

        filmStorage.update(film);

        log.info("Пользователь с id = {} успешно удалил лайк фильму с id = {}", user.getId(), film.getId());
    }

    public Collection<Film> getPriorityList(int count) {
        if (count < 0) {
            throw new ValidationException("Count должен быть положительным");
        }

        Collection<Film> filmsPriority = findAll().stream()
                .sorted(Comparator.comparing((Film film) -> {
                    Set<Long> likes = film.getLikes();
                    return likes == null ? 0 : likes.size();
                }).reversed())
                .limit(count)
                .toList();

        log.info("Успешно получен список из первых фильмов по количеству лайков размером: {}", filmsPriority.size());
        return filmsPriority;
    }*/
}