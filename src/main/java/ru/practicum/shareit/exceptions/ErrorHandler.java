package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(InvalidEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleInvalidEmailException(InvalidEmailException e) {
        return new ExceptionMessage("400", e.getMessage());
    }

    @ExceptionHandler({NoSuchUserException.class, WrongUserIdException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleNoSuchUserAndWrongUserException(NoSuchUserException e) {
        return new ExceptionMessage("404", e.getMessage());
    }

    @ExceptionHandler(InvalidItemInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleInvalidItemInputException(InvalidItemInputException e) {
        return new ExceptionMessage("400", e.getMessage());
    }


}
