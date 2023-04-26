package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemService {

    ItemDto create(ItemDto itemDto) throws ObjectNotFoundException;

    CommentDto createComment(CommentDto commentDto) throws ValidationException;

    ItemDto update(ItemDto itemDto) throws ValidationException;

    Item getById(Long id) throws ObjectNotFoundException;

    Item getById(Long id, Long ownerId) throws ObjectNotFoundException;

    void delete(Long id) throws ObjectNotFoundException;

    List<Item> getAll(Long ownerId, Integer from, Integer size);

    List<Item> getByFilter(String textFilter, Long userId, Integer from, Integer size);

}
