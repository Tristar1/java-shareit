package ru.practicum.shareit.request;

import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {

    ItemRequest create(ItemRequestDto requestDto) throws ValidationException, ObjectNotFoundException;

    ItemRequestDto getById(Long userId, Long id) throws ObjectNotFoundException;

    List<ItemRequestDto> getAll(Long userId);

    List<ItemRequestDto> getAll(Long ownerId, Integer from, Integer size);

}
