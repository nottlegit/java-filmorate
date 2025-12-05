package ru.yandex.practicum.filmorate.exception;

public class InconsistentDataException extends RuntimeException {
    NotFoundException notFoundException;

    public InconsistentDataException(long user1, long user2, NotFoundException e) {
        super(String.format("Нарушение целостности данных: пользователь %d " +
                        "имеет в друзьях несуществующего пользователя %d",
                user1, user2));

        notFoundException = e;
    }
}
