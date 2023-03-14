package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
}
