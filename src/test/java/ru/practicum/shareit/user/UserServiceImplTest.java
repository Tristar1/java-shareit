package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository repository;

    @Test
    void create() throws ValidationException {
        User user = new User(1, "name", "user@user.com");
        UserDto response = UserMapper.mapToUserDto(user);
        userService.create(response);
        verify(repository, times(1)).save(UserMapper.mapToNewUser(response));
    }

    @Test
    void update() throws ValidationException {
        User user = new User(1, "newName", "newUser@user.com");
        UserDto response = UserMapper.mapToUserDto(user);
        when(repository.findById(1)).thenReturn(Optional.of(UserMapper.mapToNewUser(response)));
        userService.update(response);
        verify(repository, times(1)).save(UserMapper.mapToNewUser(response));
    }

    @Test
    void getById() throws ValidationException {
        User user = new User(1, "newName", "newUser@user.com");
        UserDto response = UserMapper.mapToUserDto(user);
        userService.create(response);
        when(repository.findById(1)).thenReturn(Optional.of(UserMapper.mapToNewUser(response)));
        userService.getById(1);
        verify(repository, times(1)).findById(1);
    }

    @Test
    void delete() {
        userService.delete(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void getAll() {
        User user = new User(1, "newName", "newUser@user.com");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(repository.findAll()).thenReturn(userList);
        userService.getAll();
        verify(repository, times(1)).findAll();
    }
}