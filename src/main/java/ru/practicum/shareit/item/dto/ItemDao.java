package ru.practicum.shareit.item.dto;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

public interface ItemDao {

    Item itemFromData(ItemDto itemDto);

    ItemDto itemToData(Item item);

    Item updateItemFromData(ItemDto itemDto, Item item) throws ValidationException;
}
