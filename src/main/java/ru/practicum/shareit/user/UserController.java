package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.getUserStorage().getAll();
    }

    @PostMapping
    public User create(@RequestBody UserDto user) throws ValidationException {
        return userService.getUserStorage().create(user);
    }

    @PatchMapping("/{userId}")
    public User update(@RequestBody UserDto userDto, @PathVariable("userId") Integer userId) throws ValidationException {
        UserStorage userStorage = userService.getUserStorage();
        userDto.setId(userId);
        return userStorage.update(userDto);
    }

    @DeleteMapping
    public Boolean deleteUser(@Valid @RequestBody User user) {
        userService.getUserStorage().delete(user.getId());
        return true;
    }

    @DeleteMapping("/{userId}")
    public Boolean deleteUserById(@PathVariable("userId") Integer userId) {
        userService.getUserStorage().delete(userId);
        return true;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getUserById(@PathVariable("id") Integer id) {
        return userService.getUserStorage().getById(id);
    }
}
