package ru.practicum.shareit.exceptions;

public class NoSuchBookingException extends RuntimeException {
    public NoSuchBookingException(String message) {
        super(message);
    }
}
