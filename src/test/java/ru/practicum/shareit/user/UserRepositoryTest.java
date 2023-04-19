package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Test
    void find() {
        User user = User.builder()
                .name("newName")
                .email("newUser@user.com")
                .build();
        entityManager.persist(user);
        List<User> result = repository.findAll();
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(user));
    }

}
