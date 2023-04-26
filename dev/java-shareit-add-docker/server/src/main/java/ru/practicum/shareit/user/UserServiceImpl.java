package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Optional;

@Primary
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return userRepository.save(user);
    }

    @Override
    public User update(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + userDto.getId()));
        UserMapper.updateUserFields(user, userDto);
        return userRepository.save(user);
    }

    @Override
    public User getById(Long id) throws ObjectNotFoundException {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь с id " + id + " не найден!");
        }

        return user.get();
    }

    @Override
    public void delete(Long id) throws ObjectNotFoundException {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

}
