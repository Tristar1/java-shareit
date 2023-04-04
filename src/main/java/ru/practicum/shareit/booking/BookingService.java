package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

public interface BookingService {

    Booking create(BookingDto bookingDto) throws ValidationException;

    Booking update(BookingDto bookingDto) throws ValidationException;

    Booking getById(Integer id) throws ObjectNotFoundException;

    Booking getByIdAndUserId(Integer id, Integer userId) throws ObjectNotFoundException;

    void delete(Integer id) throws ObjectNotFoundException;

    List<Booking> getAll(Integer bookerId, String state) throws ValidationException;

    List<Booking> getAllByOwner(Integer ownerId, String state) throws ObjectNotFoundException, ValidationException;

    BookingRepository getBookingRepository();
}
