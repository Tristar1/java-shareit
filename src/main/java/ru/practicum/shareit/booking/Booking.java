package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class Booking {
    Integer id;
    LocalDateTime start;
    LocalDateTime end;
    @NotNull
    Item item;
    @NotNull
    User booker;
    @Builder.Default
    Status status = Status.WAITING;
}
