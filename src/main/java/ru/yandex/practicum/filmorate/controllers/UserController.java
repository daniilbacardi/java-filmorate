package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 0;

    @PostMapping
    public User addUser(@RequestBody User user) {
        user.setId(userId++);
        UserValidator.validate(user);
        users.put(user.getId(), user);
        log.info("пользователь добавлен", user);
        return user;

    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserValidator.validate(user);
        users.put(user.getId(), user);
        log.info("пользователь обновлен", user);
        return user;
    }

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }
}