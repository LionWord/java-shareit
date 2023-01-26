package ru.practicum.shareit.exceptions;

public class NoSuchRequestException extends RuntimeException {
    public NoSuchRequestException(String message) {
        super(message);
    }
}
