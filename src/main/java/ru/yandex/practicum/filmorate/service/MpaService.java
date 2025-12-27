package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaRepository mpaRepository;

    public Collection<MpaDto> getAllMpaRatings() {
        log.debug("Получение списка всех MPA рейтингов");
        return mpaRepository.findAll().stream()
                .map(MpaMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить MPA рейтинг по ID
     * @throws NotFoundException если рейтинг не найден
     */
    public MpaDto getMpaRatingById(Long id) {
        log.debug("Получение MPA рейтинга по ID: {}", id);
        return mpaRepository.findById(id)
                .map(MpaMapper::mapToMpaDto)
                .orElseThrow(() -> new NotFoundException("MPA рейтинг с ID " + id + " не найден"));
    }
}