package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;


public interface ItemService {

    ItemDto create(ItemDto itemDto);

    CommentDto createComment(CommentDto commentDto) throws ValidationException;

    ItemDto update(ItemDto itemDto) throws ValidationException;

    Item getById(Long id, LocalDateTime dateTime);

    Item getById(Long id, Long ownerId, LocalDateTime dateTime);

    void delete(Long id);

    List<Item> getAll(Long ownerId, Integer from, Integer size, LocalDateTime dateTime);

    List<Item> getByFilter(String textFilter, Long userId, Integer from, Integer size);

}
