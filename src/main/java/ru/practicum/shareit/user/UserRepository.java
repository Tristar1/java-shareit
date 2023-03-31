package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exception.ObjectNotFoundException;


public interface UserRepository extends JpaRepository<User, Integer> {

}
