package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserStorage userStorage;

    public UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
