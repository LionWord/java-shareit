package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
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
            AlreadyApprovedException.class,
            EmptyCommentException.class,
            CantCommentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleRuntimeException(RuntimeException e) {
        log.debug("Throwing " + e.getClass().getName() + "Message: " + e.getMessage());
        return new ExceptionMessage(e.getMessage());
    }

    @ExceptionHandler({NoSuchUserException.class,
            NoSuchItemException.class,
            NoSuchBookingException.class,
            WrongUserIdException.class,
            BookingSelfOwnedItemException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleNotFoundException(RuntimeException e) {
        log.debug("Throwing " + e.getClass().getName() + "Message: " + e.getMessage());
        return new ExceptionMessage(e.getMessage());
    }


}
