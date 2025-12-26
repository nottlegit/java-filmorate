package ru.yandex.practicum.filmorate.dto.friendship;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.yandex.practicum.filmorate.model.friendship.FriendshipStatus;

@Builder(toBuilder = true)
public class FriendshipDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull(message = "User ID не может быть пустым")
    private Long userId;
    @NotNull(message = "Friend ID не может быть пустым")
    private Long friendId;

    @NotNull(message = "Статус дружбы не может быть пустым")
    private FriendshipStatus status;
}
