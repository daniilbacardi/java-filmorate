package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
        return userService.addNewUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriendToUser(@PathVariable int id, @PathVariable int friendId) {
        userService.addUserToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteUserFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.removeUserFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsByUserId(@PathVariable int id) {
        return userService.getAllUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getUsersCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getUsersCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }
}