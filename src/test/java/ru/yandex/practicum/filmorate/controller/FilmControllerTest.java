package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryUserStorage;

class FilmControllerTest {
    private FilmController filmController;
    private FilmService filmService;
    private InMemoryFilmStorage filmStorage;
    private InMemoryUserStorage userStorage;
    private Film validFilm;
    private Validator validator;

    @BeforeEach
    void setUp() {
        // Инициализируем зависимости вручную
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);

        validFilm = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120L)
                .build();

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Должны создаться 2 фильма")
    void testShouldCreateFilms() {
        Film film1 = filmController.create(validFilm);
        Film film2 = filmController.create(validFilm.toBuilder().name("Film 2").build());

        assertEquals(1L, film1.getId());
        assertEquals(2L, film2.getId());
    }

    @Test
    @DisplayName("Должно храниться 2 фильма")
    void testShouldGetAllFilms() {
        filmController.create(validFilm);
        filmController.create(validFilm.toBuilder().name("Film 2").build());

        Collection<Film> films = filmController.getFilms();

        assertEquals(2, films.size());
    }

    @Test
    @DisplayName("Обновление фильма")
    void testShouldUpdateFilm() {
        Film createdFilm = filmController.create(validFilm);

        Film updatedFilm = createdFilm.toBuilder()
                .name("Updated Film")
                .description("Updated Description")
                .build();

        Film result = filmController.update(updatedFilm);

        assertEquals("Updated Film", result.getName());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    @DisplayName("Должно быть сгенерировано исключение, когда фильм не найден")
    void testShouldThrowExceptionWhenUpdatingNonExistentFilm() {
        Film nonExistentFilm = validFilm.toBuilder().id(123L).build();

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> filmController.update(nonExistentFilm));

        System.out.println(exception.getMessage());
        assertEquals("Фильм с id = 123 не найден", exception.getMessage());
    }

    @Test
    @DisplayName("Получение пустого списка фильмов")
    void testShouldHandleEmptyFilmList() {
        Collection<Film> films = filmController.getFilms();

        assertTrue(films.isEmpty());
    }

    @Test
    @DisplayName("Фильм с датой релиза 1895.12.27 не должен добавиться")
    void testShouldFailWhenReleaseDateIsBefore1895() {
        Film film = filmController.create(validFilm.toBuilder()
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build());

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Фильм с датой релиза 1895.12.28 должен добавиться")
    void testShouldPassWhenReleaseDateIsValid() {
        Film film = filmController.create(validFilm.toBuilder()
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build());

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }
}