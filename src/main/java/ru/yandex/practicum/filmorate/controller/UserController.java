package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final List<User> users = new ArrayList<>();
    private int nextId = 1;

    @GetMapping
    public List<User> findAll() {
        log.info("Получен запрос на получение всех пользователей");
        return users;
    }

    @PostMapping
    public User create(@Validated(Create.class) @RequestBody User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(nextId++);
        users.add(user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Validated(Update.class) @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя: {}", user);
        if (user.getId() == 0) {
            throw new ValidationException("Id пользователя должен быть указан");
        }
        User existingUser = users.stream()
                .filter(u -> u.getId() == user.getId())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + user.getId() + " не найден"));

        // Обновляем только те поля, которые были переданы (не null)
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getLogin() != null) {
            existingUser.setLogin(user.getLogin());
        }
        // Обработка имени: если передано и не пустое — устанавливаем; если передано пустое — подставляем логин
        if (user.getName() != null) {
            if (user.getName().isBlank()) {
                existingUser.setName(existingUser.getLogin());
            } else {
                existingUser.setName(user.getName());
            }
        }
        if (user.getBirthday() != null) {
            existingUser.setBirthday(user.getBirthday());
        }

        log.info("Пользователь обновлен: {}", existingUser);
        return existingUser;
    }
}