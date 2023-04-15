package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Common;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    Item item;
    User user;
    UserDto userDto;
    ItemDto itemDto;
    ItemRequest request;
    ItemRequestDto requestDto;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RequestService itemRequestService;
    @MockBean
    private ItemService itemService;

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
    void getAll() throws Exception {
        List<ItemRequest> itemRequests = new ArrayList<>();

        request = RequestMapper.mapToRequest(requestDto);
        itemRequests.add(request);
        when(itemRequestService.getAll(userDto.getId()))
                .thenReturn(itemRequests);

        this.mockMvc
                .perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void findAll() throws Exception {
        List<ItemRequest> itemRequests = new ArrayList<>();

        request = RequestMapper.mapToRequest(requestDto);
        itemRequests.add(request);
        when(itemRequestService.getAll(userDto.getId(), 0, 1))
                .thenReturn(itemRequests);

        this.mockMvc
                .perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void create() throws Exception {
        requestDto.setId(1);
        String json = Common.requestDtoToJson(requestDto);

        request = RequestMapper.mapToRequest(requestDto);

        when(itemRequestService.create(requestDto))
                .thenReturn(request);

        this.mockMvc
                .perform(post("/requests")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getRequestById() throws Exception {

        requestDto.setId(1);
        request = RequestMapper.mapToRequest(requestDto);

        when(itemRequestService.getById(user.getId(), requestDto.getId()))
                .thenReturn(request);

        this.mockMvc
                .perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
}