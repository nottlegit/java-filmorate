package ru.yandex.practicum.filmorate.model.friendship;

import lombok.Getter;

@Getter
public enum FriendshipStatus {
    PENDING("pending"),    // запрос отправлен, но не подтвержден
    CONFIRMED("confirmed");

    private final String value;

    FriendshipStatus(String value) {
        this.value = value;
    }

    public static FriendshipStatus fromString(String value) {
        for (FriendshipStatus status : FriendshipStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус дружбы: " + value);
    }
}
