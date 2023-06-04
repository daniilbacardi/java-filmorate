package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@Valid @PathVariable int id) {
        log.info("Выполнен запрос(GET) на получение фильма с ID={}", id);
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        log.info("Выполнен запрос(POST) на добавление фильма {}", film);
        return filmService.addNewFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Выполнен запрос(PUT) на обновление фильма {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Выполнен запрос(PUT) на добавление пользователем с ID={} лайка фильму с ID={}", userId, id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Выполнен запрос(DELETE) на удаление лайка пользователя с ID={} у фильма с ID={}", userId, id);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Выполнен запрос(GET) на получение ТОП-{} популярных фильмов", count);
        return filmService.popularFilms(count);
    }
}