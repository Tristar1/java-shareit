package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.exception.ValidationException;

@Component
public class BookingValidator {

    public static void valid(BookItemRequestDto booking) throws ValidationException {

        if (booking.getStart().isAfter(booking.getEnd())
                || booking.getStart() == booking.getEnd()
                || booking.getStart().equals(booking.getEnd())
                || booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("Время начала должно быть раньше времени окончания бронирования!");
        }
    }
}
