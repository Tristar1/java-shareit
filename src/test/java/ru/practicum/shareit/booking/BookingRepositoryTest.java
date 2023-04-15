package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class BookingRepositoryTest {

    User owner;
    User booker;
    Item item;
    Sort sort = Sort.by(Sort.Direction.DESC, "start");
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BookingRepository repository;

    @BeforeEach
    void setUp() {
        owner = User.builder().name("newName").email("newUser@user.com").build();
        booker = User.builder().name("requestor").email("requestor@user.com").build();
        item = Item.builder()
                .name("Дрель")
                .description("description")
                .available(true)
                .owner(owner)
                .build();
        entityManager.persist(owner);
        entityManager.persist(booker);
        entityManager.persist(item);
    }

    @Test
    void findByIdAndAndBooker_Id() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        Booking result = repository.findByIdAndAndBooker_Id(booking.getId(), booker.getId());
        then(result).isEqualTo(booking);
    }

    @Test
    void findByIdAndOwner() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        Booking result = repository.findByIdAndOwner(booking.getId(), owner.getId());
        then(result).isEqualTo(booking);
    }

    @Test
    void findAllByBooker_Id() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        List<Booking> result = repository.findAllByBooker_Id(booker.getId(), sort);
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void findAllByBooker_IdAndStatus() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        List<Booking> result = repository.findAllByBooker_IdAndStatus(booker.getId(), Status.WAITING, sort);
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void findAllByEndBefore() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        List<Booking> result = repository.findAllByEndBefore(LocalDateTime.now().plusDays(2), sort);
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void findAllByStartAfter() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        List<Booking> result = repository.findAllByStartAfter(LocalDateTime.now(), sort);
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void findAllByItemOwner() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        List<Booking> result = repository.findAllByItemOwner(owner.getId());
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void findAllByItemOwnerAndStatus() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        List<Booking> result = repository.findAllByItemOwnerAndStatus(owner.getId(), Status.WAITING.toString());
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void findAllByItemOwnerAndEndBefore() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        List<Booking> result = repository.findAllByItemOwnerAndEndBefore(owner.getId(), LocalDateTime.now().plusDays(3));
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void findAllByItemOwnerAndStartAfter() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        List<Booking> result = repository.findAllByItemOwnerAndStartAfter(owner.getId(), LocalDateTime.now());
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void findCurrentOwnerBookings() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        List<Booking> result = repository.findCurrentOwnerBookings(owner.getId(), LocalDateTime.now().plusMinutes(15));
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void havingBookingsAtCurrentDate() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.APPROVED).build();
        entityManager.persist(booking);
        List<Booking> result = repository.havingBookingsAtCurrentDate(item.getId(), LocalDateTime.now().plusHours(2));
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void findFirstByByItemIdAndStartAfter() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.APPROVED).build();
        entityManager.persist(booking);
        Optional<Booking> result = repository.findFirstByByItemIdAndStartAfter(item.getId(), LocalDateTime.now());
        then(result).isEqualTo(Optional.of(booking));
    }

    @Test
    void findFirstByItemIdAndEndBefore() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.APPROVED).build();
        entityManager.persist(booking);
        Optional<Booking> result = repository.findFirstByItemIdAndEndBefore(item.getId(), LocalDateTime.now().plusDays(2));
        then(result).isEqualTo(Optional.of(booking));
    }

    @Test
    void findAllByBooker_IdAndStatusAndEndBefore() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        List<Booking> result = repository.findAllByBooker_IdAndStatusAndEndBefore(booker.getId(),
                Status.WAITING, LocalDateTime.now().plusDays(15));
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }

    @Test
    void findCurrentBookings() {
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING).build();
        entityManager.persist(booking);
        List<Booking> result = repository.findCurrentBookings(booker.getId(), LocalDateTime.now().plusMinutes(15));
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(booking));
    }
}