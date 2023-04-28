package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")  Integer from,
                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        return requestClient.getItemRequestsAll(ownerId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestBody @Valid ItemRequestDto requestDto) {
        requestDto.setRequestorId(userId);
        return requestClient.createItemRequest(requestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable("requestId") Long id,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getItemRequest(id, userId);
    }

}
