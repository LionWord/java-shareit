package ru.practicum.shareit.exceptions;

public class BookingSelfOwnedItemException extends RuntimeException{
    public BookingSelfOwnedItemException(String message) {
        super(message);
    }
}
