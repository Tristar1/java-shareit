package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserStorage {

    User create(UserDto user) throws ValidationException;

    User update(UserDto user) throws ValidationException;

    User getById(Integer id) throws ObjectNotFoundException;

    void delete(Integer id) throws ObjectNotFoundException;

    List<User> getAll();
}
