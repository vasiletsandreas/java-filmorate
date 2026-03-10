package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    private int id;

    // При создании: обязательно и должно быть корректным email
    @NotBlank(groups = Create.class, message = "Электронная почта не может быть пустой")
    @Email(groups = Create.class, message = "Электронная почта должна содержать символ @")
    // При обновлении: если поле передано, то должно быть корректным email
    @Email(groups = Update.class, message = "Электронная почта должна содержать символ @")
    private String email;

    // Логин всегда должен быть заполнен и без пробелов (для всех операций)
    @NotBlank(groups = {Create.class, Update.class}, message = "Логин не может быть пустым")
    @Pattern(groups = {Create.class, Update.class}, regexp = "\\S+", message = "Логин не может содержать пробелы")
    private String login;

    // Имя для отображения (может быть пустым — тогда подставится логин)
    private String name;

    // Дата рождения всегда должна быть в прошлом
    @Past(groups = {Create.class, Update.class}, message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}