package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;

@Component
public class BookingValidator {

    public static void valid(Booking booking) throws ValidationException,ObjectNotFoundException {
        if (booking.getStart() == null) {
            throw new ValidationException("Время начала брони должно быть обязательно заполнено!");
        }
        if (booking.getEnd() == null) {
            throw new ValidationException("Время окончания брони должно быть обязательно заполнено!");
        }
        if (booking.getStatus() == null) {
            throw new ValidationException("Статус брони должен быть обязательно заполнено!");
        }
        if (booking.getStart().isAfter(booking.getEnd())
                || booking.getStart() == booking.getEnd()
                || booking.getStart().equals(booking.getEnd())
                || booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("Время начала должно быть раньше времени окончания бронирования!");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())
                || booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время начала и окончания не должно быть раньше текущего времени!");
        }
        if (booking.getItem() == null) {
            throw new ValidationException("Предмет брони должен быть обязательно заполнен!");
        }
        if (booking.getBooker() == null) {
            throw new ValidationException("Арендатор должен быть обязательно заполнен!");
        }
        if (!booking.getItem().getAvailable()) {
            throw new ValidationException("Данный предмет не доступен в аренду!");
        }
        if (booking.getItem().getOwner().equals(booking.getBooker())) {
            throw new ObjectNotFoundException("Невозможно брать в аренду собственные предметы!");
        }
    }
}
