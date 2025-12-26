package ru.yandex.practicum.filmorate.model.friendship;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Friendship {
    @NotNull(message = "User ID не может быть пустым")
    private Long userId;
    @NotNull(message = "Friend ID не может быть пустым")
    private Long friendId;

    @NotNull(message = "Статус дружбы не может быть пустым")
    private FriendshipStatus status;
}
