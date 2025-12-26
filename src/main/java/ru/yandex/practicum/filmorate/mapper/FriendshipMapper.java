package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.friendship.FriendshipDto;
import ru.yandex.practicum.filmorate.dto.friendship.NewFriendshipRequest;
import ru.yandex.practicum.filmorate.model.friendship.Friendship;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendshipMapper {
    public static Friendship mapToFriendship(NewFriendshipRequest request) {
        return Friendship.builder()
                .userId(request.getUserId())
                .friendId(request.getFriendId())
                .status(request.getStatus())
                .build();
    }

    public static FriendshipDto mapToFriendshipDto(Friendship friendship) {
        return FriendshipDto.builder()
                .userId(friendship.getUserId())
                .friendId(friendship.getFriendId())
                .status(friendship.getStatus())
                .build();
    }
}
