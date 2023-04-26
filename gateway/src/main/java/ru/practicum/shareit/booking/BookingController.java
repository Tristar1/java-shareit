package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		Status state = Status.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size, LocalDateTime.now());
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") Long userId,
											@RequestBody @Valid BookItemRequestDto requestDto) throws ValidationException {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		BookingValidator.valid(requestDto);
		requestDto.setBookerId(userId);
		return bookingClient.bookItem(requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
												@PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getByOwner(@RequestHeader("X-Sharer-User-Id") Long bookerId,
									@RequestParam(name = "state", defaultValue = "ALL") String state,
									@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
									@Positive @RequestParam(name = "size", defaultValue = "25") Integer size) {
		return bookingClient.getBookingsByOwner(bookerId, state.toUpperCase(), from, size, LocalDateTime.now());
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
						  				@PathVariable("bookingId") Long bookingId,
						  				@RequestParam(name = "approved") Boolean status) {
		return bookingClient.update(bookingId, userId, status);
	}
}
