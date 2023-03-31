package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<Item> findAll(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemService.getAll(ownerId);
    }

    @PostMapping
    public Item create(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @RequestBody ItemDto itemDto) throws ValidationException {
        itemDto.setOwnerId(ownerId);
        return itemService.create(itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @RequestBody ItemDto itemDto, @PathVariable("itemId") Integer itemId) throws ValidationException {
        itemDto.setOwnerId(ownerId);
        itemDto.setId(itemId);
        return itemService.update(itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable("itemId") Integer itemId,
                                 @RequestBody CommentDto commentDto) throws ValidationException {
        commentDto.setAuthorId(userId);
        commentDto.setItemId(itemId);
        return itemService.createComment(commentDto);
    }

    @DeleteMapping
    public Boolean deleteItem(@Valid @RequestBody ItemDto itemDto) {
        itemService.delete(itemDto.getId());
        return true;
    }

    @DeleteMapping("/{itemId}")
    public Boolean deleteItemById(@PathVariable("itemId") Integer itemId) {
        itemService.delete(itemId);
        return true;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Item getItemById(@PathVariable("id") Integer id,
                            @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return (!(ownerId == null)) ? itemService.getById(id, ownerId) : itemService.getById(id);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Item> getByTextFilter(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @RequestParam String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemService.getByFilter(text, ownerId);
    }
}
