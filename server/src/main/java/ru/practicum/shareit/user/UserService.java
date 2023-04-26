package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    User create(UserDto userDto);

    User update(UserDto userDto) throws ObjectNotFoundException;

    User getById(Long id) throws ObjectNotFoundException;

    void delete(Long id) throws ObjectNotFoundException;

    List<User> getAll();
}
