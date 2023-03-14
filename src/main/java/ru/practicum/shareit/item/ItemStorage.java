package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    public Item create(ItemDto itemDto) throws ValidationException;

    public Item update(ItemDto itemDto) throws ValidationException;

    public Item getById(Integer id) throws ObjectNotFoundException;

    public void delete(Integer id) throws ObjectNotFoundException;

    public List<Item> getAll(Integer ownerId);

    public List<Item> getByFilter(String textFilter);
}
