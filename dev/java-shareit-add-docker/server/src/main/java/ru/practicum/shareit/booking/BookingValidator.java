package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;

@Component
public class BookingValidator {

    public static void valid(Booking booking) throws ValidationException,ObjectNotFoundException {

        if (!booking.getItem().getAvailable()) {
            throw new ValidationException("Данный предмет не доступен в аренду!");
        }
        if (booking.getItem().getOwner().equals(booking.getBooker())) {
            throw new ObjectNotFoundException("Невозможно брать в аренду собственные предметы!");
        }
    }
}
