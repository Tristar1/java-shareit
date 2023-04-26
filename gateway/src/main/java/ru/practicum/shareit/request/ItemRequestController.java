package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;

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
                                     @RequestParam(name = "from", defaultValue = "1")  Integer from,
                                     @RequestParam(name = "size", defaultValue = "10") Integer size) throws ValidationException {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Неверно заполнены параметры постраничного просмотра!");
        }
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
