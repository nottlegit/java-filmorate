package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreFilmsService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreFilmsController {
    private final GenreFilmsService genreFilmsService;

    @GetMapping
    public Collection<FilmGenre> getAllGenres() {
        return genreFilmsService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Collection<FilmGenre> getGenresById(@PathVariable Long id) {
        return genreFilmsService.getGenresById(id);
    }
}
