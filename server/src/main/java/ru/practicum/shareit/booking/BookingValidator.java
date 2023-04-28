package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

@Component
public class BookingValidator {

    public static void valid(Booking booking) throws ValidationException {

        if (!booking.getItem().getAvailable()) {
            throw new ValidationException("Данный предмет не доступен в аренду!");
        }
        if (booking.getItem().getOwner().equals(booking.getBooker())) {
            throw new NotFoundException("Невозможно брать в аренду собственные предметы!");
        }
    }
}
