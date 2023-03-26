package ru.practicum.shareit.user.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
@Slf4j
public class UserInMemoryImpl implements UserDao {

    @Override
    public User userFromData(UserDto userDto) {
        return User.builder().id(userDto.id).email(userDto.email).name(userDto.name).build();
    }

    @Override
    public UserDto userToData(User user) {
        return null;
    }

    @Override
    public User updateUserFromData(UserDto userDto, User user) {
        User newUser = new User();
        newUser.setId(user.getId());
        if (userDto.email != null && !userDto.email.isBlank()) {
            newUser.setEmail(userDto.email);
        } else {
            newUser.setEmail(user.getEmail());
        }
        if (userDto.name != null && !userDto.name.isBlank()) {
            newUser.setName(userDto.name);
        } else {
            newUser.setName(user.getName());
        }

        return newUser;
    }
}
