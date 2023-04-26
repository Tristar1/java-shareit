package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    public static ItemRequestDto toRequestDto(ItemRequest request) {

        return ItemRequestDto.builder()
                .requestorId(request.getRequestor().getId())
                .description(request.getDescription())
                .id(request.getId())
                .created(request.getCreated())
                .items((request.getItems() == null) ? new ArrayList<>() : ItemMapper.toItemDto(request.getItems()))
                .build();
    }

    public static List<ItemRequestDto> toRequestDto(Iterable<ItemRequest> requests) {
        List<ItemRequestDto> result = new ArrayList<>();

        for (ItemRequest request : requests) {
            result.add(toRequestDto(request));
        }

        return result;
    }

    public static ItemRequest toRequest(ItemRequestDto requestDto) {

        return ItemRequest.builder()
                .description(requestDto.getDescription())
                .id(requestDto.getId())
                .created((requestDto.getCreated() == null) ? LocalDateTime.now() : requestDto.getCreated())
                .build();
    }

    public static void updateRequestFields(ItemRequest request, ItemRequestDto requestDto) {

        if (requestDto.getDescription() != null && !requestDto.getDescription().isBlank()) {
            request.setDescription(requestDto.getDescription());

        }
    }
}
