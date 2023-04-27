package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {

        return ItemDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner().getId())
                .name(item.getName())
                .requestId((item.getRequest() == null) ? null : item.getRequest().getId())
                .build();
    }

    public static List<ItemDto> toItemDto(Iterable<Item> items) {
        List<ItemDto> result = new ArrayList<>();

        for (Item item : items) {
            result.add(toItemDto(item));
        }

        return result;
    }

    public static Item toItem(ItemDto itemDto) {

        return Item.builder()
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .name(itemDto.getName())
                .build();
    }

    public static void updateItemFields(Item item, ItemDto itemDto) {

        if (!Objects.equals(itemDto.getOwnerId(), item.getOwner().getId())) {
            throw new NotFoundException("Невозможно поменять владельца предмета!");
        }

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
    }

}
