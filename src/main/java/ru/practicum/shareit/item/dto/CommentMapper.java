package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static CommentDto mapToCommentDto(Comment comment) {

        return CommentDto.builder()
                .authorName(comment.getAuthor().getName())
                .authorId(comment.getAuthor().getId())
                .itemId(comment.getItem().getId())
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentDto> mapToCommentDto(Iterable<Comment> comments) {
        List<CommentDto> result = new ArrayList<>();

        for (Comment comment : comments) {
            result.add(mapToCommentDto(comment));
        }

        return result;
    }

    public static Comment mapToNewComment(CommentDto commentDto) {

        return Comment.builder()
                .text(commentDto.getText())
                .created(LocalDateTime.now())
                .build();
    }
}
