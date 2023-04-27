package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final RequestService requestService;

    @GetMapping
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getAll(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "1") Integer from,
                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return requestService.getAll(ownerId, from, size);
    }

    @PostMapping
    public ItemRequest create(@RequestBody ItemRequestDto requestDto) throws ValidationException {
        return requestService.create(requestDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable("requestId") Long id,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getById(userId, id);
    }

}
