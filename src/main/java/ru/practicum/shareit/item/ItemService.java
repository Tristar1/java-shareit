package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemService {

    ItemDto create(ItemDto itemDto) throws ValidationException;

    CommentDto createComment(CommentDto commentDto) throws ValidationException;

    ItemDto update(ItemDto itemDto) throws ValidationException;

    Item getById(Integer id) throws ObjectNotFoundException;

    Item getById(Integer id, Integer ownerId) throws ObjectNotFoundException;

    void delete(Integer id) throws ObjectNotFoundException;

    List<Item> getAll(Integer ownerId, Integer from, Integer size);

    List<Item> getByFilter(String textFilter, Integer userId, Integer from, Integer size);

}
