package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MPA (Motion Picture Association) рейтинг
 */
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mpa {

    private Long id;

    @NotBlank(message = "Код MPA рейтинга не может быть пустым")
    private String code;

    @NotBlank(message = "Название MPA рейтинга не может быть пустым")
    private String name;

    private String description;
}