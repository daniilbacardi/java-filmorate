package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        log.info("Выполнен запрос(POST) на добавление пользователя {}", user);
        return userService.addNewUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Выполнен запрос(PUT) на обновление пользователя {}", user);
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Выполнен запрос(PUT) на добавление пользователя с ID={} " +
                "в друзья к пользователю с ID={}", friendId, id);
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Выполнен запрос(DELETE) на удаление пользователя с ID={} " +
                "из друзей пользователя с ID={}", friendId, id);
        userService.removeFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("Выполнен запрос(GET) на получение всех друзей пользователя с ID={}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Выполнен запрос(GET) на получение общих друзей пользователей с ID={} и ID={}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    User getUserById(@PathVariable int id) {
        log.info("Выполнен запрос(GET) на получение пользователя с ID={}", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Выполнен запрос(GET) на получение всех пользователей");
        return userService.getAllUsers();
    }
}