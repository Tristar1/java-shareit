package ru.practicum.shareit.item.dto;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

public interface ItemDao {

    public Item itemFromData(ItemDto itemDto);

    public ItemDto itemToData(Item item);

    public Item updateItemFromData(ItemDto itemDto, Item item) throws ValidationException;
}
