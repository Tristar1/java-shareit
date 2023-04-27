package ru.practicum.shareit.handler;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

}
