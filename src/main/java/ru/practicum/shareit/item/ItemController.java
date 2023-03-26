package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
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
        return itemService.getItemStorage().getAll(ownerId);
    }

    @PostMapping
    public Item create(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @RequestBody ItemDto itemDto) throws ValidationException {
        itemDto.setOwnerId(ownerId);
        return itemService.getItemStorage().create(itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @RequestBody ItemDto itemDto, @PathVariable("itemId") Integer itemId) throws ValidationException {
        ItemStorage itemStorage = itemService.getItemStorage();
        itemDto.setOwnerId(ownerId);
        itemDto.setId(itemId);
        return itemStorage.update(itemDto);
    }

    @DeleteMapping
    public Boolean deleteItem(@Valid @RequestBody Item item) {
        itemService.getItemStorage().delete(item.getId());
        return true;
    }

    @DeleteMapping("/{itemId}")
    public Boolean deleteItemById(@PathVariable("itemId") Integer itemId) {
        itemService.getItemStorage().delete(itemId);
        return true;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Item getItemById(@PathVariable("id") Integer id) {
        return itemService.getItemStorage().getById(id);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Item> getReviewByFilmId(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @RequestParam String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemService.getItemStorage().getByFilter(text.toLowerCase());
    }
}
