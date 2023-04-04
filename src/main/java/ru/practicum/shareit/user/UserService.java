package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

public interface UserService {

    User create(UserDto userDto) throws ValidationException;

    User update(UserDto userDto) throws ValidationException;

    User getById(Integer id) throws ObjectNotFoundException;

    void delete(Integer id) throws ObjectNotFoundException;

    List<User> getAll();
}
