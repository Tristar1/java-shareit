package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Common;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    UserDto userDto;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void findAll() throws Exception {
        userDto = new UserDto(1, "update", "update@mail.com");

        List<User> userDtoCollection = new ArrayList<>();
        userDtoCollection.add(UserMapper.mapToNewUser(userDto));
        when(userService.getAll())
                .thenReturn(userDtoCollection);

        this.mockMvc
                .perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void create() throws Exception {
        userDto = new UserDto(1, "name", "user@user.com");
        String json = Common.userToJson(UserMapper.mapToNewUser(userDto));
        when(userService.create(any()))
                .thenReturn(UserMapper.mapToNewUser(userDto));

        this.mockMvc.perform(post("/users")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("user@user.com")));
    }

    @Test
    void update() throws Exception {
        userDto = new UserDto(1, "update", "user@user.com");
        String json = Common.userToJson(UserMapper.mapToNewUser(userDto));
        this.mockMvc
                .perform(patch("/users/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        this.mockMvc
                .perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserById() throws Exception {
        this.mockMvc
                .perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById() throws Exception {
        userDto = new UserDto(1, "update", "update@mail.com");

        when(userService.getById(1))
                .thenReturn(UserMapper.mapToNewUser(userDto));

        this.mockMvc
                .perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("update@mail.com")));
    }
}