package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentDto {

    private Integer id;
    private String text;
    private String authorName;
    private Integer authorId;
    private Integer itemId;
    private LocalDateTime created;

}
