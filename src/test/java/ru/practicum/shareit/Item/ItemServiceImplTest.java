package ru.practicum.shareit.Item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class ItemServiceImplTest {

    Item item;
    User user;
    Comment comment;
    UserDto userDto;
    ItemDto itemDto;
    CommentDto commentDto;
    Booking booking;
    List<Booking> bookings;
    @Autowired
    private ItemService service;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        user = new User(1, "name", "user@user.com");
        userDto = new UserDto(1, "name", "user@user.com");
        booking = Booking.builder().id(1).build();
        bookings = new ArrayList<>();
        bookings.add(booking);
        itemDto = ItemDto.builder()
                .id(1)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .ownerId(1)
                .build();

    }

    @Test
    void create() throws ValidationException {

        item = ItemMapper.mapToItem(itemDto);
        item.setOwner(user);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        service.create(itemDto);
        verify(itemRepository, times(1)).save(item);

    }

    @Test
    void createComment() throws ValidationException {

        item = ItemMapper.mapToItem(itemDto);
        item.setOwner(user);
        item.setId(1);

        comment = Comment.builder()
                .created(LocalDateTime.now())
                .text("Comment")
                .id(1)
                .author(user)
                .item(item)
                .build();

        commentDto = CommentMapper.mapToCommentDto(comment);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBooker_IdAndStatusAndEndBefore(anyInt(),
                any(Status.class), any())).thenReturn(bookings);

        service.createComment(commentDto);
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void update() throws ValidationException {

        item = ItemMapper.mapToItem(itemDto);
        item.setId(1);
        item.setOwner(user);

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        service.update(itemDto);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void getById() {

        item = ItemMapper.mapToItem(itemDto);
        item.setId(1);
        item.setOwner(user);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        service.getById(1, 1);

    }

    @Test
    void delete() {

        item = ItemMapper.mapToItem(itemDto);
        when(itemRepository.findById(1)).thenReturn(Optional.ofNullable(item));
        service.delete(1);
        verify(itemRepository, times(1)).deleteById(1);

    }

    @Test
    void getAll() {

        item = ItemMapper.mapToItem(itemDto);
        item.setId(1);
        item.setOwner(user);
        List<Item> items = List.of(item);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(1)).thenReturn(items);
        service.getAll(1, 0, 1);
    }

    @Test
    void getByFilter() {

        item = ItemMapper.mapToItem(itemDto);
        List<Item> items = List.of(item);
        when(itemRepository.findAllByTextFilter(
                "аККум", 1)).thenReturn(items);
        service.getByFilter("аККум", 1, 0, 1);
    }
}