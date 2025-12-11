package ru.yandex.practicum.filmorate.exception;

public class NotFriendsException extends RuntimeException {
    public NotFriendsException(long user1, long user2) {
        super(String.format("Пользователи %d и %d не друзья", user1, user2));
    }
}
