package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;

    public Collection<FilmDto> getFilms() {
        Collection<FilmDto> collection = filmRepository.findAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());

        log.info("Успешно получены все фильмы. Текущее количество {}", collection.size());
        return collection;
    }

    public FilmDto getFilmById(long filmId) {
        if (filmId < 0) {
            String errorMessage = "ID фильма должен быть положительным";
            log.warn("Ошибка при обновлении фильма: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        FilmDto filmDto = filmRepository.findById(filmId)
                        .map(FilmMapper::mapToFilmDto)
                        .orElseThrow(() -> new NotFoundException("Фильм не найден с ID: " + filmId));

        log.info("Фильм успешно получен по id: {}", filmId);
        return filmDto;
    }

    public FilmDto createFilm(NewFilmRequest request) {
        Film film = FilmMapper.mapToFilm(request);

        log.info("{}", film.getMpa());

        if ((film.getMpa().getId() < 1 || film.getMpa().getId() > 5)) {
            throw new NotFoundException("Id mpa должен быть не больше 5 и больше 0");
        }

        film = filmRepository.save(film);

        log.info("Фильм успешно добавлен: {}", film);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto updateFilm(UpdateFilmRequest request) {
        log.info("Обновление фильма id={}", request.getId());

        Film updatedFilm = filmRepository.findById(request.getId())
                        .map(film -> FilmMapper.updateUserFields(film, request))
                        .orElseThrow(() -> new NotFoundException("Фильм не найден с ID: " + request.getId()));

        updatedFilm = filmRepository.update(updatedFilm);
        log.info("Фильм успешно обновлен: {}", updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    /*public void addLike(long filmId, long userId) {
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