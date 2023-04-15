package ru.practicum.shareit.Item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository repository;

    @Test
    void findAllByOwnerId() {
        User user = User.builder().name("newName").email("newUser@user.com").build();
        Item item = Item.builder()
                .name("Дрель")
                .description("description")
                .available(true).owner(user)
                .build();
        entityManager.persist(user);
        entityManager.persist(item);
        List<Item> result = repository.findAllByOwnerId(user.getId());
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(item));
    }

    @Test
    void findAllByTextFilter() {
        User owner = User.builder().name("newName").email("newUser@user.com").build();
        Item item = Item.builder()
                .name("Дрель")
                .description("дрель аккумуляторная")
                .available(true)
                .owner(owner)
                .build();
        entityManager.persist(owner);
        entityManager.persist(item);
        List<Item> result = repository.findAllByTextFilter("АккУМ", 1);
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(item));
    }

    @Test
    void findAllByRequest_Id() {

        User owner = User.builder().name("newName").email("newUser@user.com").build();
        User requestor = User.builder().name("requestor").email("requestor@user.com").build();
        ItemRequest itemRequest = ItemRequest.builder().
                description("descr").
                created(LocalDateTime.now()).
                requestor(requestor)
                .build();
        Item item = Item.builder()
                .name("Дрель")
                .description("дрель аккумуляторная")
                .available(true)
                .owner(owner)
                .request(itemRequest)
                .build();
        entityManager.persist(owner);
        entityManager.persist(requestor);
        entityManager.persist(itemRequest);
        entityManager.persist(item);
        List<Item> result = repository.findAllByRequest_Id(itemRequest.getId());
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(item));
    }
}