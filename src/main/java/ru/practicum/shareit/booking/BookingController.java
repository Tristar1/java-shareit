package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<Booking> findAll(@RequestHeader("X-Sharer-User-Id") Integer bookerId,
                                 @RequestParam(name = "state", defaultValue = "ALL") String state) throws ValidationException {
        return bookingService.getAll(bookerId, state.toUpperCase());
    }

    @GetMapping("/owner")
    public List<Booking> getByOwner(@RequestHeader("X-Sharer-User-Id") Integer bookerId,
                                    @RequestParam(name = "state", defaultValue = "ALL") String state) throws ValidationException {
        return bookingService.getAllByOwner(bookerId, state);
    }

    @PostMapping
    @ResponseBody
    public Booking create(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @RequestBody BookingDto bookingDto) throws ValidationException {
        bookingDto.setBookerId(ownerId);
        return bookingService.create(bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseBody
    public Booking update(@RequestHeader("X-Sharer-User-Id") Integer userId,
                          @PathVariable("bookingId") Integer bookingId,
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
    public Boolean deleteBookingById(@PathVariable("bookingId") Integer bookingId) {
        bookingService.delete(bookingId);
        return true;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @PathVariable("id") Integer id) {
        return bookingService.getByIdAndUserId(id, userId);
    }
}
