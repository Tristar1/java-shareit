package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectExistException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.Objects;

@Component
public class UserValidator {

    public void valid(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Почта пользователя должна быть обязательно заполнена");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Почта пользователя должна обязательно содержать символ @!");
        }

    }
}
