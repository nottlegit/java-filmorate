package ru.yandex.practicum.filmorate.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GenreDto {
    private Long id;
    private String name;
}