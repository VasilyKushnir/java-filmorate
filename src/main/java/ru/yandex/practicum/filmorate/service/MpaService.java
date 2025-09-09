package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    public Collection<MpaDto> getAll() {
        return mpaStorage.findAll()
                .stream()
                .map(MpaMapper::mapToMpaDto)
                .toList();
    }

    public MpaDto getMpa(Integer mpaId) {
        return mpaStorage.findMpa(mpaId)
                .map(MpaMapper::mapToMpaDto)
                .orElseThrow(() -> new NotFoundException("Genre with id = " + mpaId + " was not found"));
    }

    public boolean isMpaExists(Integer mpaId) {
        return mpaStorage.findMpa(mpaId).isPresent();
    }
}