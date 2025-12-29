package ru.yandex.practicum.filmorate.dto.mpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO для отдачи MPA рейтинга в API
 */
@Data
@AllArgsConstructor
@Builder
public class MpaDto {
    private Long id;
    private String name;
    private String description;
}