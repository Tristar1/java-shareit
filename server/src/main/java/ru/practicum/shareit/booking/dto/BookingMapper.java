package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {

        return BookingDto.builder()
                .id(booking.getId())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .start(booking.getStart())
                .status(booking.getStatus().toString())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public static List<BookingDto> toBookingDto(Iterable<Booking> bookings) {
        List<BookingDto> result = new ArrayList<>();

        for (Booking booking : bookings) {
            result.add(toBookingDto(booking));
        }

        return result;
    }

    public static Booking toBooking(BookingDto bookingDto) {

        return Booking.builder()
                .end(bookingDto.getEnd())
                .start(bookingDto.getStart())
                .status(bookingDto.getStatus() == null ? Status.WAITING : Status.valueOf(bookingDto.getStatus()))
                .build();
    }

    public static void updateBookingFields(Booking booking, BookingDto bookingDto) {

        if (bookingDto.getStart() != null) {
            booking.setStart(bookingDto.getStart());
        }
        if (bookingDto.getEnd() != null) {
            booking.setEnd(bookingDto.getEnd());
        }
        if (bookingDto.getStatus() != null) {
            booking.setStatus(Status.valueOf(bookingDto.getStatus()));
        }

    }

}
