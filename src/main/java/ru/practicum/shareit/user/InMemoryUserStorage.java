package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDao;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> userList;
    private final UserValidator userValidator;
    private final UserDao userDao;
    private Integer maxId = 0;

    public InMemoryUserStorage(UserDao userDao) {
        this.userList = new HashMap<>();
        this.userValidator = new UserValidator(this);
        this.userDao = userDao;
    }

    @Override
    public User create(UserDto userDto) throws ValidationException {
        User user = userDao.userFromData(userDto);
        userValidator.valid(user);
        user.setId(++maxId);
        userList.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(UserDto userDto) throws ValidationException {
        User user = userDao.updateUserFromData(userDto, getById(userDto.getId()));
        userValidator.valid(user);
        userList.put(user.getId(), user);
        return userList.get(user.getId());
    }

    @Override
    public User getById(Integer id) throws ObjectNotFoundException {
        if (userList.get(id) == null) {
            throw new ObjectNotFoundException("Пользователь с ID " + id + " не существует!");
        }
        return userList.get(id);
    }

    @Override
    public void delete(Integer id) throws ObjectNotFoundException {
        getById(id);
        userList.remove(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userList.values());
    }

}
