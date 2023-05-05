package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addUserToFriends(int userId, int friendId) {
        if (!userStorage.getAllUsers().contains(userStorage.getUserById(userId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", friendId));
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUserById(friendId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        } else {
            userStorage.getUserById(userId).getFriends().add(friendId);
            userStorage.getUserById(friendId).getFriends().add(userId);
            log.info("{} и {} подружились",
                    userStorage.getUserById(userId).getName(),
                    userStorage.getUserById(friendId).getName());
        }
    }

    public void removeUserFromFriends(int userId, int friendId) {
        if (!userStorage.getAllUsers().contains(userStorage.getUserById(userId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        }
        if (!userStorage.getAllUsers().contains(userStorage.getUserById(friendId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("нет пользователя с id %d", userId));
        } else {
            userStorage.getUserById(userId).getFriends().remove(friendId);
            userStorage.getUserById(friendId).getFriends().remove(userId);
            log.info("{} и {} больше не дружат",
                    userStorage.getUserById(userId).getName(),
                    userStorage.getUserById(friendId).getName());
        }
    }

    public List<User> getAllUserFriends(int userId) {
        return userStorage.getUserById(userId).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public Set<User> getUsersCommonFriends(int userId, int friendId) {
        return userStorage.getUserById(userId).getFriends().stream()
                .filter(u -> userStorage.getUserById(friendId).getFriends().contains(u))
                .map(userStorage::getUserById)
                .collect(Collectors.toSet());
    }

    public User addNewUser(User user) {
        userStorage.addNewUser(user);
        return user;
    }

    public User updateUser(User user) {
        userStorage.updateUser(user);
        return user;
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
}
