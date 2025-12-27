package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmLikeRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final FilmLikeRepository filmLikeRepository;

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

    public void addLike(long filmId, long userId) {
        if (userId < 0 || filmId < 0) {
            throw new ValidationException("ID должен быть положительным");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", userId)
                ));

        Film film = filmRepository.findById(filmId).orElseThrow(
                () -> new NotFoundException("Фильм не найден. Id: " + filmId)
        );

        FilmLike filmLike = FilmLike.builder()
                .filmId(film.getId())
                .userId(user.getId())
                .build();

        filmLike = filmLikeRepository.save(filmLike);


        log.info("Пользователь с id = {} успешно поставил лайк фильму с id = {}", filmLike.getId(), filmLike.getId());
    }

    public void deleteLike(long filmId, long userId) {
        if (userId < 0 || filmId < 0) {
            throw new ValidationException("ID должен быть положительным");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id=%d не найден", userId)
                ));

        Film film = filmRepository.findById(filmId).orElseThrow(
                () -> new NotFoundException("Фильм не найден. Id: " + filmId)
        );

        FilmLike filmLike = filmLikeRepository.findByFilmIdAndUserId(film.getId(), user.getId()).orElseThrow(
                () -> new NotFoundException(String.format(
                        "Лайк фильма с id = %d. И пользователем id = %d не найден",
                        filmId,
                        userId
                ))
        );

        if (filmLikeRepository.deleteByFilmIdAndUserId(filmLike)) {
            log.info("Пользователь с id = {} успешно удалил лайк фильму с id = {}", user.getId(), film.getId());
        }
    }

    public Collection<FilmDto> getPriorityList(int limit) {
        if (limit < 0) {
            throw new ValidationException("Count должен быть положительным");
        }

        Collection<Long> topFilmIds = filmLikeRepository.findTopFilmsByLikes(limit);

        List<FilmDto> filmsPriority = topFilmIds.stream()
                .map(filmRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());

        log.info("Успешно получен список из первых фильмов по количеству лайков размером: {}", filmsPriority.size());
        return filmsPriority;
    }
}