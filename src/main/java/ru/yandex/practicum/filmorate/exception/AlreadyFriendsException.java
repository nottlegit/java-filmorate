package ru.yandex.practicum.filmorate.exception;

public class AlreadyFriendsException extends RuntimeException {
    public AlreadyFriendsException(long user1, long user2) {
        super(String.format("Пользователи %d и %d уже друзья", user1, user2));
    }
}