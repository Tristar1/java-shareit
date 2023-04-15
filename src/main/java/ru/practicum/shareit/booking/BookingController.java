package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
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
    public List<Booking> findAll(@RequestHeader("X-Sharer-User-Id") Integer bookerId,
                                 @RequestParam(name = "state", defaultValue = "ALL") String state,
                                 @RequestParam(name = "from", defaultValue = "0") Integer from,
                                 @RequestParam(name = "size", defaultValue = "25") Integer size) throws ValidationException {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Неверно заполнены параметры постраничного просмотра!");
        }
        return bookingService.getAll(bookerId, state.toUpperCase(), from, size, LocalDateTime.now());
    }

    @GetMapping("/owner")
    public List<Booking> getByOwner(@RequestHeader("X-Sharer-User-Id") Integer bookerId,
                                    @RequestParam(name = "state", defaultValue = "ALL") String state,
                                    @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @RequestParam(name = "size", defaultValue = "25") Integer size) throws ValidationException {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Неверно заполнены параметры постраничного просмотра!");
        }
        return bookingService.getAllByOwner(bookerId, state.toUpperCase(), from, size, LocalDateTime.now());
    }

    @PostMapping
    @ResponseBody
    public Booking create(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                          @RequestBody BookingDto bookingDto) throws ValidationException {
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
