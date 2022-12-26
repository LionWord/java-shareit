package ru.practicum.shareit.exceptions;

public class WrongUserIdException extends RuntimeException {
    public WrongUserIdException(String message) {
        super(message);
    }
}
