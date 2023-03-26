package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ObjectExistException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.Objects;

public class UserValidator {

    private final UserStorage userStorage;

    public UserValidator(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public void valid(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Почта пользователя должна быть обязательно заполнена");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Почта пользователя должна обязательно содержать символ @!");
        }

        for (User userOfList : userStorage.getAll()) {
            if (userOfList.getEmail().equals(user.getEmail()) && !Objects.equals(userOfList.getId(), user.getId())) {
                throw new ObjectExistException("Пользователь с почтой " + user.getEmail() + " уже существует!");
            }
        }
    }

}
