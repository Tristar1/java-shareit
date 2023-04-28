package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentRequestDto {

    private Long id;
    @NotNull(message = "Комментарий не должен быть NULL!")
    @NotBlank(message = "Комментарий не должен быть пустой!")
    private String text;
    private String authorName;
    private Long authorId;
    private Long itemId;
    private LocalDateTime created;

}
