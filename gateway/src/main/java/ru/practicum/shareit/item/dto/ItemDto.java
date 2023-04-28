package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ItemDto {

    @NotNull(message = "Название вещи не должно быть NULL!")
    @NotBlank(message = "Название вещи не должно быть пустым!")
    private String name;
    private Long id;
    @NotNull(message = "Описание вещи не должно быть NULL!")
    @NotBlank(message = "Описание вещи не должно быть пустым!")
    private String description;
    @NotNull(message = "Доступность вещи должна быть заполнена!")
    private Boolean available;
    private Long ownerId;
    private Long requestId;

}
