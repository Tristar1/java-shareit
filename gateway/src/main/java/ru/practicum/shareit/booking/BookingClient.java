package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.client.BaseClient;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(long userId, Status state, Integer from, Integer size, LocalDateTime dateTime) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size,
                "dateTime", dateTime
        );
        return get("?state={state}&from={from}&size={size}&dateTime={dateTime}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwner(Long userId, String state, Integer from, Integer size, LocalDateTime dateTime) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size,
                "dateTime", dateTime
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> update(Long bookingId, Long userId, Boolean state) {
        Map<String, Object> parameters = Map.of(
                "approved", state.toString()
        );
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> bookItem(BookItemRequestDto requestDto) {
        return post("", requestDto);
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }
}
