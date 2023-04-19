package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;

@Component
public class CommentValidator {

    public static void valid(Comment comment) throws ValidationException {

        if (comment.getText() == null || comment.getText().isBlank()) {
            throw new ValidationException("Комментарий не может быть пустым!");
        }

    }
}
