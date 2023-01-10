package ru.practicum.shareit.exceptions;

public class WrongTimestampException extends RuntimeException{
    public WrongTimestampException(String message) {
        super(message);
    }
}
