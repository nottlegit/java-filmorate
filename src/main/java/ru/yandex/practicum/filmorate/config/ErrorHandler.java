package ru.yandex.practicum.filmorate.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.yandex.practicum.filmorate.dto.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.*;

import java.lang.IllegalArgumentException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({
            ValidationException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestExceptions(Exception e) {
        return new ErrorResponse("Ошибка запроса", e.getMessage());
    }

    @ExceptionHandler({
            AlreadyFriendsException.class,
            NotFriendsException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictExceptions(Exception e) {
        return new ErrorResponse("Конфликт данных", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        return new ErrorResponse("Объект не найден", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    public ErrorResponse handleInconsistentData(InconsistentDataException e) {
        return new ErrorResponse("Внутренняя ошибка сервера", e.getMessage());
    }
}
