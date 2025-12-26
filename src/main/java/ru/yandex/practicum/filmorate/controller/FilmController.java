package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDto> getFilms() {
        log.info("Получен запрос на получение всех фильмов.");
        return filmService.getFilms();
    }

    @GetMapping("/{filmId}")
    public FilmDto findById(@PathVariable long filmId) {
        log.info("Получен запрос на получение фильма по id: {}", filmId);
        return filmService.getFilmById(filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto create(@Valid @RequestBody NewFilmRequest filmRequest) {
        log.info("Получен запрос на добавление нового фильма: {}", filmRequest);
        return filmService.createFilm(filmRequest);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest updateFilmRequest) {
        log.info("Получен запрос на обновление фильма: {}", updateFilmRequest);
        return filmService.updateFilm(updateFilmRequest);
    }


    /*@PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        log.info("Добавление лайка: фильм={}, пользователь={}", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        log.info("Удаление лайка: фильм={}, пользователь={}", filmId, userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPriorityList(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Получение списка фильмов по количеству лайков: count = {}", count);
        return filmService.getPriorityList(count);
    }*/
}