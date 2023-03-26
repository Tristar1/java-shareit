package ru.practicum.shareit.user.dto;
import ru.practicum.shareit.user.User;

public interface UserDao {

    public User userFromData(UserDto userDto);

    public UserDto userToData(User user);

    public User updateUserFromData(UserDto userDto, User user);

}
