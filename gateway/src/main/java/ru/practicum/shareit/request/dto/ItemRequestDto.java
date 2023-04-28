package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ItemRequestDto {

    private Long id;
    @NotNull(message = "Описание вещи в заапросе не должно быть NULL!")
    @NotBlank(message = "Описание вещи в заапросе не должно быть пустым!")
    private String description;
    private Long requestorId;
    private LocalDateTime created;
    List<ItemDto> items;
}
