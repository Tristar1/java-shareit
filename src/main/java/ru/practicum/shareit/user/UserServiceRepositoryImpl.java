package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Optional;

@Primary
@Service
@RequiredArgsConstructor
public class UserServiceRepositoryImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    public User create(UserDto userDto) throws ValidationException {
        User user = UserMapper.mapToNewUser(userDto);
        userValidator.valid(user);
        return userRepository.save(user);
    }

    @Override
    public User update(UserDto userDto) throws ValidationException {
        User user = userRepository.findById(userDto.getId()).orElseThrow();
        updateUserFields(user, userDto);
        userValidator.valid(user);
        return userRepository.save(user);
    }

    private void updateUserFields(User user, UserDto userDto) {

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
    }

    @Override
    public User getById(Integer id) throws ObjectNotFoundException {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь с id " + id + " не найден!");
        }

        return user.get();
    }

    @Override
    public void delete(Integer id) throws ObjectNotFoundException {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

}
