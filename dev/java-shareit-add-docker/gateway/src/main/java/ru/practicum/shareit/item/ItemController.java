package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                          @Positive @RequestParam(name = "size", defaultValue = "25") Integer size) {
        return itemClient.getItems(ownerId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                         @RequestBody @Valid ItemDto itemDto) {
        itemDto.setOwnerId(ownerId);
        return itemClient.createItem(itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                         @RequestBody ItemDto itemDto,
                                         @PathVariable("itemId") Long itemId) {
        itemDto.setOwnerId(ownerId);
        itemDto.setId(itemId);
        return itemClient.updateItem(itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("itemId") Long itemId,
                                 @RequestBody @Valid CommentRequestDto commentDto) {
        commentDto.setAuthorId(userId);
        commentDto.setItemId(itemId);
        return itemClient.createComment(commentDto);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteItem(@RequestBody ItemDto itemDto) {
        return itemClient.deleteItem(itemDto.getId());
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItemById(@PathVariable("itemId") Long itemId) {
        return itemClient.deleteItem(itemId);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> getItemById(@PathVariable("id") Long id,
                                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return (!(ownerId == null)) ? itemClient.getItem(id, ownerId) : itemClient.getItem(id);
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<Object> getByTextFilter(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @RequestParam String text,
                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                      @Positive @RequestParam(name = "size", defaultValue = "25") Integer size) {
        if (text.isBlank()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return itemClient.getItemsByFilter(text, ownerId, from, size);
    }
}
