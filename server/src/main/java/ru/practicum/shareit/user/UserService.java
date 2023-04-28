package ru.practicum.shareit.user;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> create(UserDto userDto);

    Optional<User> update(UserDto userDto);

    Optional<User> getById(Long id);

    void delete(Long id);

    List<User> getAll();
}
