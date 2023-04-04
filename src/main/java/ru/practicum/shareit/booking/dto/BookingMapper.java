package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto mapToBookingDto(Booking booking) {

        return BookingDto.builder()
                .id(booking.getId())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .start(booking.getStart())
                .status(booking.getStatus().toString())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public static List<BookingDto> mapToBookingDto(Iterable<Booking> bookings) {
        List<BookingDto> result = new ArrayList<>();

        for (Booking booking : bookings) {
            result.add(mapToBookingDto(booking));
        }

        return result;
    }

    public static Booking mapToNewBooking(BookingDto bookingDto) {

        return Booking.builder()
                .end(bookingDto.getEnd())
                .start(bookingDto.getStart())
                .status(bookingDto.getStatus() == null ? Status.WAITING : Status.valueOf(bookingDto.getStatus()))
                .build();
    }

}
