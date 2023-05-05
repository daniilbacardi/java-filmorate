package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addNewLikeToFilm(int filmId, int userId) {
        if (!filmStorage.getAllFilms().contains(filmStorage.getFilmById(filmId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет фильма с id %d", filmId));
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUserById(userId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        } else {
            filmStorage.getFilmById(filmId).getLikes().add((userId));
            filmStorage.getFilmById(filmId).setLikeCount(filmStorage.getFilmById(filmId).getLikes().size());
            log.info("пользователю {} понравился фильм {}", userStorage.getUserById(userId).getName(),
                    filmStorage.getFilmById(filmId).getName());
        }
    }

    public void deleteLikeOnFilm(int filmId, int userId) {
        if (!filmStorage.getAllFilms().contains(filmStorage.getFilmById(filmId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет фильма с id %d", filmId));
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUserById(userId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        } else {
            filmStorage.getFilmById(filmId).getLikes().remove(userId);
            filmStorage.getFilmById(filmId).setLikeCount(filmStorage.getFilmById(filmId).getLikes().size());
            log.info("Пользователю {} больше не нравится фильм {}", userStorage.getUserById(userId).getName(),
                    filmStorage.getFilmById(filmId).getName());
        }
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(Film::getLikeCount).reversed())
                .distinct()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addNewFilm(Film film) {
        filmStorage.addNewFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmStorage.updateFilm(film);
        return film;
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }
}
