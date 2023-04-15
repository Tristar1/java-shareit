package ru.practicum.shareit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

public class Common {
    static ObjectMapper mapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());

    static ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    public static String userToJson(User user) {

        String json;
        try {
            json = ow.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            json = "";
        }
        return json;
    }

    public static String itemDtoToJson(ItemDto item) {

        String json;
        try {
            json = ow.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            json = "";
        }
        return json;
    }

    public static String commentDtoToJson(CommentDto commentDto) {

        String json;
        try {
            json = ow.writeValueAsString(commentDto);
        } catch (JsonProcessingException e) {
            json = "";
        }
        return json;
    }

    public static String bookingDtoToJson(BookingDto bookingDto) {

        String json;
        try {
            json = ow.writeValueAsString(bookingDto);
        } catch (JsonProcessingException e) {
            json = "";
        }
        return json;
    }

    public static String requestDtoToJson(ItemRequestDto requestDto) {

        String json;
        try {
            json = ow.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            json = "";
        }
        return json;
    }

}
