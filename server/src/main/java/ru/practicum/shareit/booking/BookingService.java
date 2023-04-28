package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    Booking create(BookingDto bookingDto) throws ValidationException;

    Booking update(BookingDto bookingDto) throws ValidationException;

    Booking getById(Long id);

    Booking getByIdAndUserId(Long id, Long userId);

    void delete(Long id);

    List<Booking> getAll(Long bookerId, String state, Integer from, Integer size, LocalDateTime dateTime) throws ValidationException;

    List<Booking> getAllByOwner(Long ownerId, String state, Integer from, Integer size, LocalDateTime dateTime) throws ValidationException;

}
