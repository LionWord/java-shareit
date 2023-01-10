package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({InvalidEmailException.class,
            NotOwnerException.class,
            NotAvailableException.class,
            WrongTimestampException.class,
            IllegalArgumentException.class,
            UnsupportedStatusException.class,
            InvalidItemInputException.class,
            ConstraintViolationException.class,
            AlreadyApprovedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleRuntimeException(RuntimeException e) {
        return new ExceptionMessage(e.getMessage());
    }

    @ExceptionHandler({NoSuchUserException.class,
            NoSuchItemException.class,
            NoSuchBookingException.class,
            WrongUserIdException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleNotFoundException(RuntimeException e) {
        return new ExceptionMessage(e.getMessage());
    }


}
