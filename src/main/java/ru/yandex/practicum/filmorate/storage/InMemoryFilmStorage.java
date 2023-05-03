package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();
    private Integer filmId = 0;

    private Integer assignId() {
        ++filmId;
        return filmId;
    }

    @Override
    public Film addFilm(Film film) {
        if (films.containsValue(film)) {
            throw new ValidationException("такой фильм уже добавлялся");
        }
        FilmValidator.validate(film);
        film.setId(assignId());
        films.put(film.getId(), film);
        log.info("фильм {} добавлен, id={}", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        FilmValidator.validate(film);
        films.put(film.getId(), film);
        log.info("фильм {} обновлен, id={}", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film getFilm(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Фильм с id %d не найден", id));
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }
}

