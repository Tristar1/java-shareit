package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<Item> findAll(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @RequestParam(name = "from", defaultValue = "0") Integer from,
                              @RequestParam(name = "size", defaultValue = "25") Integer size,
                              @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return itemService.getAll(ownerId, from, size, dateTime);
    }

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto) {
        return itemService.create(itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto) throws ValidationException {
        return itemService.update(itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto) throws ValidationException {
        return itemService.createComment(commentDto);
    }

    @DeleteMapping
    public Boolean deleteItem(@RequestBody ItemDto itemDto) {
        itemService.delete(itemDto.getId());
        return true;
    }

    @DeleteMapping("/{itemId}")
    public Boolean deleteItemById(@PathVariable("itemId") Long itemId) {
        itemService.delete(itemId);
        return true;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Item getItemById(@PathVariable("id") Long id,
                            @RequestHeader("X-Sharer-User-Id") Long ownerId,
                            @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return (!(ownerId == null)) ? itemService.getById(id, ownerId, dateTime) : itemService.getById(id, dateTime);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Item> getByTextFilter(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @RequestParam String text,
                                      @RequestParam(name = "from", defaultValue = "0") Integer from,
                                      @RequestParam(name = "size", defaultValue = "25") Integer size) {
        return itemService.getByFilter(text, ownerId, from, size);
    }
}
