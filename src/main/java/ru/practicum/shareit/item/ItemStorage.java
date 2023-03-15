package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(ItemDto itemDto) throws ValidationException;

    Item update(ItemDto itemDto) throws ValidationException;

    Item getById(Integer id) throws ObjectNotFoundException;

    void delete(Integer id) throws ObjectNotFoundException;

    List<Item> getAll(Integer ownerId);

    List<Item> getByFilter(String textFilter);
}
