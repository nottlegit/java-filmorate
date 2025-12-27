package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {
    public static MpaDto mapToMpaDto(Mpa mpa) {
        return MpaDto.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .description(mpa.getDescription())
                .build();
    }
}
