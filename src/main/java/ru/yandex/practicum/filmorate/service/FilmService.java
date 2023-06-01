package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage,
                       @Qualifier("LikeDbStorage") LikeStorage likeStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(int filmId, int userId) {
        filmAndUserExistValid(filmId, userId);
        likeStorage.addLike(Like
                .builder()
                .filmId(filmId)
                .userId(userId)
                .build());
    }

    public void deleteLike(int filmId, int userId) {
        filmAndUserExistValid(filmId, userId);
        likeStorage.deleteLike(Like
                .builder()
                .filmId(filmId)
                .userId(userId)
                .build());
    }

    public List<Film> popularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addNewFilm(Film film) {
        FilmValidator.validate(film);
        filmStorage.addNewFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (filmExistValid(film.getId())) {
            throw new FilmNotFoundException("Такого фильма нет");
        }
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        if (filmExistValid(id)) {
            throw new FilmNotFoundException("Такого фильма нет");
        }
        return filmStorage.getFilmById(id);
    }

    private boolean filmAndUserExistValid(int filmId, int userId) {
        filmExistValid(filmId);
        if (!userStorage.getAllUsers().contains(userStorage.getUserById(userId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Нет пользователя с id %d", userId));
        }
        return false;
    }

    private boolean filmExistValid(int filmId) {
        if (!filmStorage.getAllFilms().contains(filmStorage.getFilmById(filmId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Нет фильма с id %d", filmId));
        }
        return false;
    }
}