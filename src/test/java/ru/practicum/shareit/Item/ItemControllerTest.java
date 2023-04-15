package ru.practicum.shareit.Item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Common;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    ItemDto itemDto;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .ownerId(1)
                .build();
    }

    @Test
    void findAll() throws Exception {

        Item item = ItemMapper.mapToItem(itemDto);
        item.setId(1);

        List<Item> items = new ArrayList<>();

        items.add(item);
        when(itemService.getAll(anyInt(), anyInt(), anyInt()))
                .thenReturn(items);

        this.mockMvc
                .perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void create() throws Exception {
        String json = Common.itemDtoToJson(itemDto);
        when(itemService.create(any()))
                .thenReturn(itemDto);

        this.mockMvc
                .perform(post("/items")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void update() throws Exception {

        itemDto.setName("update");
        String json = Common.itemDtoToJson(itemDto);
        when(itemService.update(itemDto))
                .thenReturn(itemDto);

        this.mockMvc
                .perform(patch("/items/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("update")));
    }

    @Test
    void createComment() throws Exception {

        User user = new User(1, "newName", "newUser@user.com");

        Item item = ItemMapper.mapToItem(itemDto);
        item.setId(1);

        Comment comment = Comment.builder()
                .id(1)
                .text("Add comment from user1")
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();

        CommentDto commentDto = CommentMapper.mapToCommentDto(comment);

        String json = Common.commentDtoToJson(commentDto);

        when(itemService.createComment(commentDto))
                .thenReturn(commentDto);

        this.mockMvc
                .perform(post("/items/1/comment")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void deleteItem() throws Exception {
        this.mockMvc
                .perform(delete("/items/1")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getItemById() throws Exception {

        Item item = ItemMapper.mapToItem(itemDto);
        item.setId(1);

        when(itemService.getById(anyInt(), anyInt()))
                .thenReturn(item);
        when(itemService.getById(anyInt()))
                .thenReturn(item);

        this.mockMvc
                .perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Дрель")));
    }

    @Test
    void getByTextFilter() throws Exception {
        List<Item> items = new ArrayList<>();
        Item item = ItemMapper.mapToItem(itemDto);
        item.setId(1);
        items.add(item);
        when(itemService.getByFilter(anyString(), anyInt(), anyInt(), anyInt()))
                .thenReturn(items);

        this.mockMvc
                .perform(get("/items/search")
                        .param("text", "aККум")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }
}