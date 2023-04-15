package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final RequestService requestService;

    @GetMapping
    @ResponseBody
    public List<ItemRequest> getAll(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.getAll(userId);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<ItemRequest> findAll(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                     @RequestParam(name = "from", defaultValue = "1") Integer from,
                                     @RequestParam(name = "size", defaultValue = "10") Integer size) throws ValidationException {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Неверно заполнены параметры постраничного просмотра!");
        }
        return requestService.getAll(ownerId, from, size);
    }

    @PostMapping
    public ItemRequest create(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestBody ItemRequestDto requestDto) throws ValidationException {
        requestDto.setRequestorId(userId);
        return requestService.create(requestDto);
    }

    @GetMapping("/{requestId}")
    @ResponseBody
    public ItemRequest getRequestById(@PathVariable("requestId") Integer id,
                                      @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestService.getById(userId, id);
    }

}
