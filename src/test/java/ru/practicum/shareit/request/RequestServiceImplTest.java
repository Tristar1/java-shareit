package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@SpringBootTest
class RequestServiceImplTest {

    Item item;
    User user;
    UserDto userDto;
    ItemDto itemDto;
    ItemRequest request;
    ItemRequestDto requestDto;

    @Autowired
    private RequestService service;
    @MockBean
    private RequestRepository requestRepository;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
        void setUp() {
            user = new User(1, "name", "user@user.com");
            userDto = new UserDto(1, "name", "user@user.com");
            itemDto = ItemDto.builder()
                    .id(1)
                    .name("Дрель")
                    .description("Простая дрель")
                    .available(true)
                    .ownerId(1)
                .build();
        requestDto = ItemRequestDto.builder()
                .description("description")
                .requestorId(1)
                .build();

    }

    @Test
    void create() throws ValidationException {
        request = RequestMapper.mapToRequest(requestDto);
        request.setRequestor(user);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(request);

        service.create(requestDto);
        verify(requestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void getById() {
        requestDto.setId(1);
        request = RequestMapper.mapToRequest(requestDto);
        request.setRequestor(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.findById(anyInt())).thenReturn(Optional.of(request));

        service.getById(user.getId(), requestDto.getId());
        verify(requestRepository, times(1)).findById(anyInt());
    }

    @Test
    void getAll() {

        request = RequestMapper.mapToRequest(requestDto);
        request.setRequestor(user);
        request.setId(1);

        List<ItemRequest> requestList = new ArrayList<>();
        requestList.add(request);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.findAllByOwnerIdOrderByCreatedDesc(user.getId())).thenReturn(requestList);

        var result = service.getAll(user.getId(), 0, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(request.getDescription(), result.stream().findFirst().get().getDescription());
    }

}