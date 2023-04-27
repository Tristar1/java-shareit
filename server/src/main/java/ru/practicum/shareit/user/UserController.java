package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

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
    public Optional<User> create(@RequestBody UserDto user) {
        return userService.create(user);
    }

    @PatchMapping
    public Optional<User> update(@RequestBody UserDto userDto) {
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
    public Optional<User> getUserById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }
}
