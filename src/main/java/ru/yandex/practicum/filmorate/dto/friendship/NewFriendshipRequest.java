package ru.yandex.practicum.filmorate.dto.friendship;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.friendship.FriendshipStatus;

@Data
public class NewFriendshipRequest {
    @NotNull(message = "User ID не может быть пустым")
    private Long userId;
    @NotNull(message = "Friend ID не может быть пустым")
    private Long friendId;

    @NotNull(message = "Статус дружбы не может быть пустым")
    private FriendshipStatus status;
}
