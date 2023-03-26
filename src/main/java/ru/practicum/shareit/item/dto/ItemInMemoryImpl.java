package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.Objects;

@Component
@Slf4j
public class ItemInMemoryImpl implements ItemDao {

    private final UserService userService;

    public ItemInMemoryImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Item itemFromData(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.id)
                .description(itemDto.description)
                .name(itemDto.name)
                .available(itemDto.available)
                .owner(userService.getUserStorage().getById(itemDto.ownerId))
                .build();
    }

    @Override
    public ItemDto itemToData(Item item) {
        return null;
    }

    @Override
    public Item updateItemFromData(ItemDto itemDto, Item item) throws ValidationException {
        Item newItem = new Item();
        newItem.setId(item.getId());
        if (!Objects.equals(itemDto.ownerId, item.getOwner().getId())) {
            throw new ObjectNotFoundException("Невозможно поменять владельца предмета!");
        } else {
            newItem.setOwner(item.getOwner());
        }

        if (itemDto.name != null && !itemDto.name.isBlank()) {
            newItem.setName(itemDto.name);
        } else {
            newItem.setName(item.getName());
        }
        if (itemDto.description != null && !itemDto.description.isBlank()) {
            newItem.setDescription(itemDto.description);
        } else {
            newItem.setDescription(item.getDescription());
        }
        if (itemDto.available != null) {
            newItem.setAvailable(itemDto.available);
        } else {
            newItem.setAvailable(item.getAvailable());
        }

        return newItem;
    }
}
