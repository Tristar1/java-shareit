package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class BookingServiceImplTest {

    Booking booking;
    BookingDto bookingDto;
    Item item;
    User user;
    User owner;
    @Autowired
    private BookingService service;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private BookingRepository repository;

    @BeforeEach
    void upSet() {
        user = User.builder().name("name").email("user@user.com").id(1).build();
        owner = User.builder().name("owner").email("owner@owner.com").id(2).build();
        item = Item.builder()
                .available(true)
                .owner(owner)
                .build();
        bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().plusMinutes(2))
                .end(LocalDateTime.now().plusDays(1))
                .bookerId(1)
                .itemId(1)
                .build();
    }

    @Test
    void create() throws ValidationException {
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.of(owner));
        service.create(bookingDto);
        booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);
        verify(repository, times(1)).save(booking);
    }

    @Test
    void update() throws ValidationException {
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.of(owner));
        bookingDto.setId(1);
        booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);
        when(repository.findByIdAndOwner(1, 1)).thenReturn(booking);

        service.update(bookingDto);
        verify(repository, times(1)).save(booking);
    }

    @Test
    void getById() {
        booking = BookingMapper.toBooking(bookingDto);
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);
        when(repository.findById(1)).thenReturn(Optional.of(booking));

        service.getById(1);
        verify(repository, times(1)).findById(1);
    }

    @Test
    void getByIdAndUserId() {
        booking = BookingMapper.toBooking(bookingDto);
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(repository.findByIdAndAndBooker_Id(1, 1)).thenReturn(booking);
        when(repository.findByIdAndOwner(1, 1)).thenReturn(booking);

        service.getByIdAndUserId(1, 1);
        verify(repository, times(2)).findByIdAndAndBooker_Id(1, 1);
    }

    @Test
    void delete() {
        booking = BookingMapper.toBooking(bookingDto);
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);
        when(repository.findById(1)).thenReturn(Optional.of(booking));
        service.delete(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void getAllByOwner() throws ValidationException {
        booking = BookingMapper.toBooking(bookingDto);
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        when(repository.findById(1)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.of(user));

        LocalDateTime dateTime = LocalDateTime.now();

        when(repository.findAllByItemOwner(2)).thenReturn(bookings);
        when(repository.findAllByItemOwnerAndStatus(2, "WAITING")).thenReturn(bookings);
        when(repository.findAllByItemOwnerAndStatus(2, "REJECTED")).thenReturn(bookings);
        when(repository.findAllByItemOwnerAndEndBefore(2, dateTime)).thenReturn(bookings);
        when(repository.findAllByItemOwnerAndStartAfter(2, dateTime)).thenReturn(bookings);
        when(repository.findCurrentOwnerBookings(2, dateTime)).thenReturn(bookings);

        service.getAllByOwner(2, "ALL", 1, 1, dateTime);
        verify(repository, times(1)).findAllByItemOwner(2);

        service.getAllByOwner(2, "PAST", 1, 1, dateTime);
        verify(repository, times(1)).findAllByItemOwnerAndEndBefore(2, dateTime);
        service.getAllByOwner(2, "FUTURE", 1, 1, dateTime);
        verify(repository, times(1)).findAllByItemOwnerAndStartAfter(2, dateTime);
        service.getAllByOwner(2, "CURRENT", 1, 1, dateTime);
        verify(repository, times(1)).findCurrentOwnerBookings(2, dateTime);
        service.getAllByOwner(2, "WAITING", 1, 1, dateTime);
        service.getAllByOwner(2, "REJECTED", 1, 1, dateTime);
        verify(repository, times(1)).findAllByItemOwnerAndStatus(2, "WAITING");
        verify(repository, times(1)).findAllByItemOwnerAndStatus(2, "REJECTED");

    }

    @Test
    void getAll() throws ValidationException {
        booking = BookingMapper.toBooking(bookingDto);
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        when(repository.findById(1)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.of(user));

        LocalDateTime dateTime = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        when(repository.findAllByBooker_Id(1, sort)).thenReturn(bookings);
        when(repository.findAllByBooker_IdAndStatus(1, Status.WAITING, sort)).thenReturn(bookings);
        when(repository.findAllByBooker_IdAndStatus(1, Status.REJECTED, sort)).thenReturn(bookings);
        when(repository.findAllByEndBefore(dateTime, sort)).thenReturn(bookings);
        when(repository.findAllByStartAfter(dateTime, sort)).thenReturn(bookings);
        when(repository.findCurrentBookings(1, dateTime)).thenReturn(bookings);

        service.getAll(1, "ALL", 1, 1, dateTime);
        verify(repository, times(1)).findAllByBooker_Id(1, sort);

        service.getAll(1, "PAST", 1, 1, dateTime);
        verify(repository, times(1)).findAllByEndBefore(dateTime, sort);
        service.getAll(1, "FUTURE", 1, 1, dateTime);
        verify(repository, times(1)).findAllByStartAfter(dateTime, sort);
        service.getAll(1, "CURRENT", 1, 1, dateTime);
        verify(repository, times(1)).findCurrentBookings(1, dateTime);
        service.getAll(1, "WAITING", 1, 1, dateTime);
        service.getAll(1, "REJECTED", 1, 1, dateTime);
        verify(repository, times(1)).findAllByBooker_IdAndStatus(1, Status.WAITING, sort);
        verify(repository, times(1)).findAllByBooker_IdAndStatus(1, Status.REJECTED, sort);
    }
}