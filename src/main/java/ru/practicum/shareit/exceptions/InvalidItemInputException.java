package ru.practicum.shareit.exceptions;

public class InvalidItemInputException extends RuntimeException {
    public InvalidItemInputException(String message) {
        super(message);
    }
}
