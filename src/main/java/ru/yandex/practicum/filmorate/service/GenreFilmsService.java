package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmGenreRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreFilmsService {
    private final FilmGenreRepository filmGenreRepository;

    public Collection<FilmGenre> getAllGenres() {
        log.debug("Получение списка всех фильмов с жанрами");
        return filmGenreRepository.findAll();
    }

    public Collection<FilmGenre> getGenresById(Long id) {
        log.debug("Получение MPA рейтинга по ID: {}", id);
        return filmGenreRepository.findByFilmId(id);
    }
}
