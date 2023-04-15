package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Common;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    Booking booking;
    BookingDto bookingDto;
    Item item;
    User user;
    User owner;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;

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
        booking = BookingMapper.toBooking(bookingDto);
        booking.setId(1);
    }

    @Test
    void findAll() {
    }

    @Test
    void getByOwner() {
    }

    @Test
    void create() throws Exception {
        String json = Common.bookingDtoToJson(bookingDto);

        when(bookingService.create(bookingDto))
                .thenReturn(booking);

        this.mockMvc
                .perform(post("/bookings")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void update() throws Exception {

        when(bookingService.update(bookingDto))
                .thenReturn(booking);

        this.mockMvc
                .perform(patch("/bookings/1")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBookingById() throws Exception {
        this.mockMvc
                .perform(delete("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingById() throws Exception {

        when(bookingService.getById(anyInt()))
                .thenReturn(booking);

        this.mockMvc
                .perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }
}