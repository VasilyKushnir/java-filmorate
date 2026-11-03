package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<MpaDto> getAll() {
        return mpaService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MpaDto getMpa(@PathVariable("id") Integer mpaId) {
        return mpaService.getMpa(mpaId);
    }
}