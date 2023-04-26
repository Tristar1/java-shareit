package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.getAll();
    }

    @PostMapping
    public User create(@RequestBody UserDto user) {
        return userService.create(user);
    }

    @PatchMapping
    public User update(@RequestBody UserDto userDto) {
        return userService.update(userDto);
    }

    @DeleteMapping
    public Boolean deleteUser(@RequestBody User user) {
        userService.delete(user.getId());
        return true;
    }

    @DeleteMapping("/{userId}")
    public Boolean deleteUserById(@PathVariable("userId") Long userId) {
        userService.delete(userId);
        return true;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }
}
