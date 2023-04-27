package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> update(UserDto userDto) {
        Optional<User> user = getById(userDto.getId());
        UserMapper.updateUserFields(user.get(), userDto);
        return Optional.of(userRepository.save(user.get()));
    }

    @Override
    public Optional<User> getById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден id " + id);
        }

        return user;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

}
