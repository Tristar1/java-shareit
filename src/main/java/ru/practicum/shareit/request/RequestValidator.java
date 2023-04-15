package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;

@Component
public class RequestValidator {

    public static void valid(ItemRequest itemRequest) throws ValidationException {
        if (itemRequest.getDescription() == null || itemRequest.getDescription().isBlank()) {
            throw new ValidationException("Описание запроса должно быть обязательно заполнено!");
        }
        if (itemRequest.getRequestor() == null) {
            throw new ValidationException("Владелец запроса должен быть заполнен!");
        }

    }

}
