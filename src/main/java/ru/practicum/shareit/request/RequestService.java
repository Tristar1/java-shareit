package ru.practicum.shareit.request;

import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {

    ItemRequest create(ItemRequestDto requestDto) throws ValidationException, ObjectNotFoundException;

    ItemRequest getById(Integer userId, Integer id) throws ObjectNotFoundException;

    List<ItemRequest> getAll(Integer userId);

    List<ItemRequest> getAll(Integer ownerId, Integer from, Integer size);

}
