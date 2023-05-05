package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new HashMap<>();
    private Integer userId = 0;

    private Integer assignId() {
        ++userId;
        return userId;
    }

    @Override
    public User addNewUser(User user) {
        UserValidator.validate(user);
        user.setId(assignId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserValidator.validate(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с id %d не найден", id));
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
