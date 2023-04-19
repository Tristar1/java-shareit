package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemValidator {

    public static void valid(Item item) throws ValidationException {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Название вещи должно быть обязательно заполнено!");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Описание вещи должно быть обязательно заполнено!");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Доступность вещи должен быть обязательно заполнен!");
        }

    }
}
