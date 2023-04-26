package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ItemDto {

    private String name;
    private Long id;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;

}
