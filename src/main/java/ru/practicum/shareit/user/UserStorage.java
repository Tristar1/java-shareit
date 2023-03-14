package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserStorage {

    public User create(UserDto user) throws ValidationException;

    public User update(UserDto user) throws ValidationException;

    public User getById(Integer id) throws ObjectNotFoundException;

    public void delete(Integer id) throws ObjectNotFoundException;

    public List<User> getAll();
}
