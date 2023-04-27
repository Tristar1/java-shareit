package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<Booking> findAll(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                 @RequestParam(name = "state", defaultValue = "ALL") String state,
                                 @RequestParam(name = "from", defaultValue = "0") Integer from,
                                 @RequestParam(name = "size", defaultValue = "25") Integer size,
                                 @RequestParam(name = "dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) throws ValidationException {

        return bookingService.getAll(bookerId, state.toUpperCase(), from, size, dateTime);
    }

    @GetMapping("/owner")
    public List<Booking> getByOwner(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                    @RequestParam(name = "state", defaultValue = "ALL") String state,
                                    @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @RequestParam(name = "size", defaultValue = "25") Integer size,
                                    @RequestParam(name = "dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) throws ValidationException {

        return bookingService.getAllByOwner(bookerId, state.toUpperCase(), from, size, dateTime);
    }

    @PostMapping
    public Booking create(@RequestBody BookingDto bookingDto) throws ValidationException {
        return bookingService.create(bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable("bookingId") Long bookingId,
                          @RequestParam(name = "approved") Boolean status) throws ValidationException {
        BookingDto bookingDto = BookingDto.builder().build();
        bookingDto.setBookerId(userId);
        bookingDto.setId(bookingId);
        bookingDto.setStatus(status == null || !status ? Status.REJECTED.toString() : Status.APPROVED.toString());
        return bookingService.update(bookingDto);
    }

    @DeleteMapping
    public Boolean deleteBooking(@Valid @RequestBody BookingDto bookingDto) {
        bookingService.delete(bookingDto.getId());
        return true;
    }

    @DeleteMapping("/{bookingId}")
    public Boolean deleteBookingById(@PathVariable("bookingId") Long bookingId) {
        bookingService.delete(bookingId);
        return true;
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable("id") Long id) {
        return bookingService.getByIdAndUserId(id, userId);
    }

}
