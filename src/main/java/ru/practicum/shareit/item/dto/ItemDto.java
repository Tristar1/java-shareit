package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDto {

    protected String name;
    protected Integer id;
    protected String description;
    protected Boolean available;
    protected Integer ownerId;
    protected Integer requestId;

}
