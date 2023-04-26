package ru.practicum.shareit.exception;

public class ObjectExistException extends RuntimeException {
    public ObjectExistException(String message) {
        super(message);
    }
}
